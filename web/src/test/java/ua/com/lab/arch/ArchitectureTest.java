package ua.com.lab.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class ArchitectureTest {

    private static final JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
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
    void core_should_not_depend_on_external_frameworks() {
        ArchRule rule = noClasses().that()
                .resideInAPackage("ua.com.lab.core..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "jakarta.servlet..", // Ні Cервлетам
                        "java.sql..",        // Ні JDBC
                        "com.fasterxml.jackson.." // Ні Jackson
                );

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
                // Web може звертатися тільки до Core
                .whereLayer("Web").mayOnlyAccessLayers("Core", "Persistence")

                // Persistence може звертатися тільки до Core (для реалізації портів)
                .whereLayer("Persistence").mayOnlyAccessLayers("Core")

                // Core не залежить ні від кого
                .whereLayer("Core").mayNotAccessAnyLayer();

        rule.check(importedClasses);
    }
}