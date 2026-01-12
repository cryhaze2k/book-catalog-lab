package ua.com.lab.web;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavalinBookApp {

    // --- 1. МОДЕЛІ ---

    public static class Book {
        public int id;
        public String title;
        public String author;
        public Book() {}
        public Book(int id, String title, String author) {
            this.id = id; this.title = title; this.author = author;
        }
    }

    public static class Comment {
        public int id;
        public int bookId;
        public String author;
        public String text;
        public long createdAt; // Timestamp

        public Comment() {}
        public Comment(int id, int bookId, String author, String text) {
            this.id = id;
            this.bookId = bookId;
            this.author = author;
            this.text = text;
            this.createdAt = System.currentTimeMillis();
        }
    }

    // --- 2. ІМІТАЦІЯ БАЗИ ДАНИХ ---
    private static final List<Book> bookDb = new ArrayList<>();
    private static final List<Comment> commentDb = new ArrayList<>();

    static {
        // Початкові книги
        bookDb.add(new Book(1, "Kobzar", "Taras Shevchenko"));
        bookDb.add(new Book(2, "Tiger Trappers", "Ivan Bahrianyi"));
    }

    public static void main(String[] args) {
        // --- 3. ЗАПУСК JAVALIN ---
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> cors.addRule(it -> it.anyHost()));
        }).start(7000); // Порт 7000 (у тебе в попередньому скріні був 8090, можеш змінити тут)

        System.out.println("SERVER STARTED: http://localhost:7000/api/books");

        // Middleware
        app.before(ctx -> System.out.println("LOG: " + ctx.method() + " " + ctx.path()));

        // --- 4. МАРШРУТИ ДЛЯ КНИГ ---

        app.get("/api/books", ctx -> ctx.json(bookDb));

        app.get("/api/books/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Optional<Book> book = bookDb.stream().filter(b -> b.id == id).findFirst();
            if (book.isPresent()) ctx.json(book.get());
            else ctx.status(HttpStatus.NOT_FOUND).result("Book not found");
        });

        app.post("/api/books", ctx -> {
            Book newBook = ctx.bodyAsClass(Book.class);
            newBook.id = bookDb.size() + 1;
            bookDb.add(newBook);
            ctx.status(HttpStatus.CREATED).json(newBook);
        });

        // --- 5. МАРШРУТИ ДЛЯ КОМЕНТАРІВ (Відновлюємо функціонал!) ---

        // GET: Отримати коментарі для конкретної книги ?bookId=1
        app.get("/api/comments", ctx -> {
            String bookIdParam = ctx.queryParam("bookId");
            if (bookIdParam != null) {
                int bookId = Integer.parseInt(bookIdParam);
                List<Comment> filtered = commentDb.stream()
                        .filter(c -> c.bookId == bookId)
                        .collect(Collectors.toList());
                ctx.json(filtered);
            } else {
                ctx.json(commentDb); // Повернути всі, якщо ID не вказано
            }
        });

        // POST: Додати коментар
        app.post("/api/comments", ctx -> {
            try {
                // Приймаємо JSON об'єкт
                Comment newComment = ctx.bodyAsClass(Comment.class);
                newComment.id = commentDb.size() + 1;
                newComment.createdAt = System.currentTimeMillis();

                commentDb.add(newComment);

                ctx.status(HttpStatus.CREATED).json(newComment);
            } catch (Exception e) {
                // Якщо прийшов не JSON, а параметри форми (як у скріншоті PowerShell)
                // Спробуємо розпарсити Form Params (fallback)
                String bookId = ctx.formParam("bookId");
                String author = ctx.formParam("author");
                String text = ctx.formParam("text");

                if (bookId != null && author != null) {
                    Comment formComment = new Comment(
                            commentDb.size() + 1,
                            Integer.parseInt(bookId),
                            author,
                            text
                    );
                    commentDb.add(formComment);
                    ctx.status(HttpStatus.CREATED).json(formComment);
                } else {
                    ctx.status(HttpStatus.BAD_REQUEST).result("Invalid data");
                }
            }
        });
    }
}