<#-- new_book.ftl -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Нова книга</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
        .container { background-color: #ffffff; padding: 20px; border-radius: 8px; border: 1px solid #ddd; max-width: 600px; margin: 0 auto; }
        .header { text-align: center; border-bottom: 2px solid #007bff; padding-bottom: 10px; margin-bottom: 20px; }
        .content { font-size: 16px; color: #333; }
        .book-info { background-color: #f9f9f9; padding: 15px; border-left: 5px solid #007bff; margin: 20px 0; }
        .rare { color: #d9534f; font-weight: bold; border: 1px dashed #d9534f; padding: 5px; display: inline-block; margin-top: 5px;}
        .footer { margin-top: 30px; font-size: 12px; color: #777; text-align: center; border-top: 1px solid #eee; padding-top: 10px; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Spring_Framework_Logo_2018.svg/2560px-Spring_Framework_Logo_2018.svg.png"
             style="width: 150px;" alt="Logo">
        <h2>Каталог поповнено!</h2>
    </div>

    <div class="content">
        <p>Вітаємо! У бібліотеку додано нову книгу.</p>

        <div class="book-info">
            <p><strong>Назва:</strong> ${title}</p>
            <p><strong>Автор:</strong> ${author}</p>
            <p><strong>Рік видання:</strong> ${year}</p>

            <#if year < 2000>
                <div class="rare">🔥 Увага! Це раритетне видання (до 2000 року).</div>
            </#if>
        </div>

        <p><em>Дата додавання: ${added?string("yyyy-MM-dd HH:mm")}</em></p>
    </div>

    <div class="footer">
        <p>BookApp © 2026. Цей лист згенеровано автоматично.</p>
    </div>
</div>
</body>
</html>