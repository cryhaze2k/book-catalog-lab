package ua.com.lab.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class ArchitectureTest {

    // Імпортуємо класи з усіх наших модулів
    private static final JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            // Додаємо Spring, щоб перевіряти залежності від нього
            .importPackages("ua.com.lab.core", "ua.com.lab.persistence", "ua.com.lab.web");

    @Test
    void core_should_not_depend_on_web_or_persistence() {
        ArchRule rule = noClasses().that()
                .resideInAPackage("ua.com.lab.core..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("ua.com.lab.web..", "ua.com.lab.persistence..");

        rule.check(importedClasses);
    }

    @Test
    void core_should_not_depend_on_external_data_or_web_frameworks() {
        ArchRule rule = noClasses().that()
                .resideInAPackage("ua.com.lab.core..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "jakarta.servlet..", // Ні Cервлетам
                        "java.sql..",        // Ні JDBC
                        "org.springframework.web..", // Ні Spring Web
                        "org.springframework.jdbc.." // Ні Spring JDBC
                );
        // (Дозволяємо залежність від org.springframework.context, @Bean, @Service)

        rule.check(importedClasses);
    }

    @Test
    void test_layered_architecture_dependencies() {
        ArchRule rule = layeredArchitecture()
                .consideringOnlyDependenciesInAnyPackage("ua.com.lab..")

                // Визначаємо наші шари
                .layer("Core").definedBy("ua.com.lab.core..")
                .layer("Persistence").definedBy("ua.com.lab.persistence..")
                .layer("Web").definedBy("ua.com.lab.web..")

                // --- Правила залежностей ---
                // Web може звертатися до Core (і Persistence для 'runtime')
                .whereLayer("Web").mayOnlyAccessLayers("Core", "Persistence")

                // Persistence може звертатися тільки до Core (для реалізації портів)
                .whereLayer("Persistence").mayOnlyAccessLayers("Core")

                // Core не залежить ні від кого
                .whereLayer("Core").mayNotAccessAnyLayer();

        rule.check(importedClasses);
    }
}