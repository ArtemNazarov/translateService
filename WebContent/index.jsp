<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <style>
        @import url('https://fonts.googleapis.com/css?family=Indie+Flower');
        h1{
        	font-family: 'Indie Flower', cursive;
        }
        #form{
        align-content:center;
        margin:auto;
        width:80%;
        }
    </style>
</head>

<body>
    <h1 align="center" style="">Translate web service</h1>
    <div id="form" style="">
        <form align="center" action="Controller" method="get">
            <p>
                <label for="fromLang">Choose language</label>
                <select name="fromLang" id="">
                    <option value="en">English</option>
                    <option value="de">Deutsch</option>
                    <option value="fr">Francais</option>
                    <option value="es">Espanol</option>
                    <option value="it">Italiano</option>
                    <option value="rus">Русский</option>
                    <option value="ua">Украiнська</option>
                </select>
            </p>
            <textarea name="textToTranslate" cols="65" rows="5" placeholder="Enter words for translate here..."></textarea>
            <p>
                <input style="padding:15px 30px; background-color:#4CAF50;color:white;border:none;font-size:20px;cursor:pointer;text-align:center;text-decoration:none;" type="submit" name="translate" value="Translate">
            </p>
            <p>
                <label for="fromLang">Choose language</label>
                <select name="fromLang" id="">
                    <option value="en">English</option>
                    <option value="de">Deutsch</option>
                    <option value="fr">Francais</option>
                    <option value="es">Espanol</option>
                    <option value="it">Italiano</option>
                    <option value="rus">Русский</option>
                    <option value="ua">Украiнська</option>
                   
                </select>
            </p>
            <textarea align="center" name="translatedWords" cols="65" rows="5" placeholder="Result"></textarea>
        </form>
    </div>
    
</body>

</html>