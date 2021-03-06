<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>ArtisTech - POSED</title>
</head>
<body>
    <div id="content" role="main">
        <section class="row colset-2-its">
            <h1>Persistent Open Source Event Dataset</h1>

            <p>Extract links from tweets, resolve shortened links, and then download the contents of the link.</p>

            <div id="controllers" role="navigation">
                <h2>Select from the following data:</h2>
                <ul>
                    <li><a href="${createLink(controller: "tweet")}">Tweets</a></li>
                    <li><a href="${createLink(controller: "link")}">Links</a></li>
                    <li><a href="${createLink(controller: "tweetUser")}">Tweet Users</a></li>
                </ul>
            </div>
        </section>
    </div>

</body>
</html>
