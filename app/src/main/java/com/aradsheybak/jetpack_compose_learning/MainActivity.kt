package com.aradsheybak.jetpack_compose_learning

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

import com.aradsheybak.jetpack_compose_learning.ui.theme.Jetpack_compose_learningTheme
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Jetpack_compose_learningTheme {
                Content()
            }
        }
    }
}

@Composable
private fun Content() {
//    SimpleText()
//    SimpleAnnotatedString()
//    AdvancedAnnotatedString()
//    SimpleButton()
//    circleButton()
//    BlobButton()
//    SimpleImage()
//    SimpleImageCoil()
//    RoundedImageCoil()
//    CircleImageCoil()
//    SimpleColumn()
//    SimpleRow()
//    SimpleBox()
    SplashScreen()

}

@Composable
private fun SimpleText() {
    Text(
//        modifier = Modifier
//            .size(300.dp)
//            .padding(10.dp)
//            .background(colorResource(R.color.teal_200)),
        text = "Hello Jetpack Compose",
        fontSize = 16.sp,
        color = colorResource(R.color.purple_500),
        fontWeight = FontWeight.ExtraBold,
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun SimpleAnnotatedString() {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(colorResource(R.color.purple_200))) {
                append("Annotated ")
            }

            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                append("Text")
            }
        }, textAlign = TextAlign.Center,
        modifier = Modifier
            .size(250.dp)
            .padding(start = 0.dp, top = 100.dp, bottom = 0.dp, end = 0.dp)
    )

}

@Composable
private fun AdvancedAnnotatedString(context: Context = LocalContext.current) {
    var textLayoutResult: TextLayoutResult? = null

    val annotatedText = buildAnnotatedString {
        append("for more info")
        pushStringAnnotation(tag = "URL", annotation = "https://google.com")
        withStyle(
            style = SpanStyle(
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.ExtraBold
            )
        ) {
            append(" click here")
        }

        pop()
    }
    var clickedUrl by remember { mutableStateOf("") }

    Text(
        modifier = Modifier
            .size(300.dp)
            .background(colorResource(R.color.teal_200))
            .padding(start = 0.dp, top = 100.dp, bottom = 0.dp, end = 0.dp)
            .clickable {
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val layoutResult = textLayoutResult ?: return@detectTapGestures
                    val position = layoutResult.getOffsetForPosition(offset)
                    annotatedText.getStringAnnotations("URL", position, position)
                        .firstOrNull()?.let { annotation ->
                            clickedUrl = annotation.item
                            Toast.makeText(
                                context,
                                "${annotation.tag} : ${annotation.item}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }
            },
        onTextLayout = { result ->
            textLayoutResult = result
        },
        text = annotatedText,
        color = colorResource(R.color.purple_500),
        textAlign = TextAlign.Center,
    )

}

@Composable
private fun SimpleButton(context: Context = LocalContext.current) {
    Button(modifier = Modifier.padding(100.dp), onClick = {
        Toast.makeText(context, "This is Simple Button", Toast.LENGTH_LONG).show()
    }) {
        Text(text = "simple Button")
    }

}

@Composable
private fun circleButton(context: Context = LocalContext.current) {
    Button(
        onClick = {

        },
        shape = CircleShape,
        modifier = Modifier.size(150.dp)
    ) {
        Text("circle")
    }

}

@Composable
fun BlobButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    val topControl = remember { Animatable(0f) }
    val bottomControl = remember { Animatable(0f) }

    val buttonWidth = 250.dp
    val buttonHeight = 80.dp

    Box(
        modifier = modifier
            .size(buttonWidth, buttonHeight)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    scope.launch {
                        launch {
                            topControl.animateTo(
                                targetValue = -30f,
                                animationSpec = tween(
                                    durationMillis = 150,
                                    easing = LinearOutSlowInEasing
                                )
                            )
                            topControl.animateTo(
                                targetValue = 0f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                            )
                        }
                        launch {
                            bottomControl.animateTo(
                                targetValue = 30f,
                                animationSpec = tween(
                                    durationMillis = 150,
                                    easing = LinearOutSlowInEasing
                                )
                            )
                            bottomControl.animateTo(
                                targetValue = 0f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                            )
                        }

                        onClick()
                    }
                })
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(0f, height / 2)
                cubicTo(
                    0f, topControl.value,
                    width, topControl.value,
                    width, height / 2
                )
                cubicTo(
                    width, height + bottomControl.value,
                    0f, height + bottomControl.value,
                    0f, height / 2
                )
                close()
            }

            drawPath(
                path = path,
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF9C27B0), Color(0xFFE040FB))
                )
            )
        }

        Text(
            text = "Liquid Button",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Composable
private fun SimpleImage() {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "simple image",
        modifier = Modifier
            .size(150.dp)
            .background(colorResource(R.color.purple_500)),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun SimpleImageCoil() {
    Image(
        painter = rememberAsyncImagePainter("https://picsum.photos/200"),
        contentDescription = "image with coil",
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(150.dp)
    )
}

@Composable
private fun RoundedImageCoil() {
    Image(
        painter = rememberAsyncImagePainter("https://picsum.photos/200"),
        contentDescription = "image with coil",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp)
            .clip(shape = RoundedCornerShape(20.dp))
    )
}

@Composable
private fun CircleImageCoil() {
    Image(
        painter = rememberAsyncImagePainter("https://picsum.photos/200"),
        contentDescription = "image with coil",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp)
            .clip(shape = CircleShape)
    )
}

@Composable
private fun SimpleColumn() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SimpleText()
        Spacer(modifier = Modifier.height(10.dp))
        SimpleImageCoil()
    }
}

@Composable
private fun SimpleRow() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SimpleText()
        Spacer(modifier = Modifier.width(10.dp))
        SimpleImageCoil()

    }
}

@Composable
private fun SimpleBox() {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(

            painter = rememberAsyncImagePainter("https://picsum.photos/200"),
            contentDescription = "image with coil",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(300.dp)
        )

        Text(
            text = "Hello Jetpack Compose",
            fontSize = 16.sp,
            color = colorResource(R.color.purple_500),
            fontWeight = FontWeight.ExtraBold,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

    }
}

@Composable
private fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.matchParentSize(),
            painter = painterResource(id = R.drawable.ic_bg_wooden),
            contentDescription = "wooden background",
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(250.dp),
                painter = painterResource(R.drawable.ic_the_noora_logo),
                contentDescription = null
            )

            Image(
                modifier = Modifier.width(300.dp),
                painter = painterResource(R.drawable.txt_the_noora),
                contentDescription = null, contentScale = ContentScale.FillWidth
            )
        }


        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Noora Solution LTD",
                color = colorResource(R.color.yellow),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "www.thenoora.com",
                color = colorResource(R.color.yellow),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold

            )

        }
    }

}

@Composable
@Preview(showBackground = true)
private fun preview() {
//    SimpleText()
//    SimpleAnnotatedString()
//    AdvancedAnnotatedString()
//    SimpleButton()
//    circleButton()
//    BlobButton()
//    SimpleImage()
//    SimpleImageCoil()
//    RoundedImageCoil()
//    CircleImageCoil()
//    SimpleColumn()
//    SimpleRow()
//    SimpleBox()
    SplashScreen()
}