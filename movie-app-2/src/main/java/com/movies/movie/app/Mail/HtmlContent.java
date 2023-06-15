package com.movies.movie.app.Mail;


public class HtmlContent {
    String HtmlContent;

    public HtmlContent(String url, String username) {
        this.HtmlContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Clicked Button Email</title>\n" +
                "    <style>\n" +
                "        /* Button Styles */\n" +
                "        .click-button {\n" +
                "            display: inline-block;\n" +
                "            background-color: #007bff;\n" +
                "            color: white;\n" +
                "            padding: 10px 20px;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "            font-family: sans-serif;\n" +
                "            font-size: 18px;\n" +
                "            font-weight: bold;\n" +
                "            box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.2);\n" +
                "        }\n" +
                "\n" +
                "        /* Clicked Animation */\n" +
                "        .clicked {\n" +
                "            animation-name: clicked-animation;\n" +
                "            animation-duration: 1s;\n" +
                "            animation-fill-mode: forwards;\n" +
                "            background-color: #00ce15;\n" +
                "            color: #ffffff;\n" +
                "        }\n" +
                "        @keyframes clicked-animation {\n" +
                "            0% {\n" +
                "                transform: scale(1);\n" +
                "            }\n" +
                "            100% {\n" +
                "                transform: scale(1.2);\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "    <script>\n" +
                "        function sendRequest() {\n" +
                "            var button = document.getElementById('click-button');\n" +
                "            button.classList.add('clicked');\n" +
                "\n" +
                "            // Send GET request or perform desired action here\n" +
                "            // Replace the following line with your own implementation\n" +
                "            console.log('GET request sent!');\n" +
                "        }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Welcome to MovieClub!</h1>\n" +
                "    <p>Hi "+username+", welcome to our app!</p>\n" +
                "    <p> Click the button to confirm that this is your Email and start having fun!</p>\n" +
                "    <a href=\""+url+"\" id=\"click-button\" onclick=\"sendRequest();\" class=\"click-button\">Confirm</a>\n" +
                "</body>\n" +
                "</html>\n";
    }

    public String getHtmlContent() {
        return HtmlContent;
    }
}
