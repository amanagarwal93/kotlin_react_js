import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.h3
import react.useEffectOnce
import react.useState

val mainScope = MainScope()

val App = FC<Props> {

    var currentVideo: Video? by useState(null)
    var unwatchedVideos: List<Video> by useState(emptyList())
    var watchedVideos: List<Video> by useState(emptyList())

    document.bgColor = "#AFD275"

    useEffectOnce {
        mainScope.launch {
            unwatchedVideos = fetchVideos()
        }
    }

    ReactHTML.div {
        h3 {
            +"Videos to watch"
        }

        VideoList {
            videos = unwatchedVideos
            selectedVideo = currentVideo
            onSelectedVideo = { video ->
                currentVideo = video
            }
        }

        h3 {
            +"Videos watched"
        }
        VideoList {
            videos = watchedVideos
            selectedVideo = currentVideo
            onSelectedVideo = { video ->
                currentVideo = video
            }
        }
    }

    currentVideo?.let { current ->
        VideoPlayer {
            video = current
            unWatchedVideo = current in unwatchedVideos
            onWatchedButtonPressed = {
                if (current in unwatchedVideos) {
                    unwatchedVideos = unwatchedVideos - video
                    watchedVideos = watchedVideos + video
                } else {
                    watchedVideos = watchedVideos - video
                    unwatchedVideos = unwatchedVideos + video
                }
            }
        }
    }
}

suspend fun fetchVideos(): List<Video> = coroutineScope {
    (1..25).map { id ->
        async {
            fetchVideo(id)
        }
    }.awaitAll()
}

suspend fun fetchVideo(id: Int): Video {
    val response = window
        .fetch("https://my-json-server.typicode.com/kotlin-hands-on/kotlinconf-json/videos/$id")
        .await()
        .text()
        .await()
    return Json.decodeFromString(response)
}