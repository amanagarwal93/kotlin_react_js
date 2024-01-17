import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3

external interface VideoPlayerProps : Props {
    var video: Video
    var onWatchedButtonPressed: (Video) -> Unit
    var unWatchedVideo: Boolean
}



val VideoPlayer = FC<VideoPlayerProps> { props ->
    div {

        css {
            position = Position.absolute
            top = 10.px
            right = 10.px
        }

        h3 {
            +"${props.video.speaker}: ${props.video.title}"
        }

        button {
//            className =
//            css {
//                display = Display.block
//                backgroundColor = if (props.unWatchedVideo) rgba(175, 210, 117, 100.0) else rgba(175, 210, 117, 100.0)
//            }

            onClick = {
                props.onWatchedButtonPressed(props.video)
            }
            if (props.unWatchedVideo) {
                +"Mark as watched"
            } else {
                +"Mark as unWatched"
            }
        }

        EmailShareButton {
            url = props.video.videoUrl
            EmailIcon {
                size = 32
                round = true
            }
        }

        TelegramShareButton {
            url = props.video.videoUrl
            TelegramIcon {
                size = 32
                round = true
            }
        }

        ReactPlayer {
            url = props.video.videoUrl
            controls = true
        }
    }
}