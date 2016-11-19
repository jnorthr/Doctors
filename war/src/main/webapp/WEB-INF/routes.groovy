get "/", forward: "/WEB-INF/pages/index.gtpl", cache: 60.seconds
get "/datetime", forward: "/datetime.groovy", cache: 10.seconds

get "/topic1", forward: "index.adoc", cache: 10.seconds
get "/topic2", forward: "index1.adoc", cache: 10.seconds

get "/topic3", forward: "index1.html", cache: 30.seconds
get "/topic4", forward: "index2.html", cache: 30.seconds

get "/topic5", forward: "/WEB-INF/index3.adoc", cache: 10.seconds
get "/topic6", forward: "/WEB-INF/index4.html", cache: 10.seconds
get "/topic7", forward: "index5.adoc", cache: 30.seconds
