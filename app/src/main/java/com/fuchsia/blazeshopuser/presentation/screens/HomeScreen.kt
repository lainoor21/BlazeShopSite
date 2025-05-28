package com.fuchsia.blazeshopuser.presentation.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.fuchsia.blazeshopuser.R
import com.fuchsia.blazeshopuser.common.FLASH_SALE
import com.fuchsia.blazeshopuser.domain.models.ProductCategoryModel
import com.fuchsia.blazeshopuser.domain.models.ProductModel
import com.fuchsia.blazeshopuser.presentation.nav.Routes
import com.fuchsia.blazeshopuser.presentation.viewModel.MyViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController
) {

    val context = LocalContext.current

    val homeCategoryState = viewModel.getHomeCategoryState.collectAsState()
    val homeCategoryList = remember { mutableStateOf(emptyList<ProductCategoryModel>()) }

    val bannerState = viewModel.getBannerState.collectAsState()
    val bannerList = remember { mutableStateOf(emptyList<ProductCategoryModel>()) }

    val homeFlashSaleState = viewModel.getHomeProductByCategoryState.collectAsState()
    val homeFlashSaleList = remember { mutableStateOf(emptyList<ProductModel>()) }

    val showUiState = remember { mutableStateOf(false) }


    LaunchedEffect(key1 = Unit) {
        if (homeCategoryState.value.isSuccess == null) {
            viewModel.getHomeCategory()
        }

        if (homeFlashSaleState.value.isSuccess == null) {
            viewModel.getHomeFlashSale(FLASH_SALE)
        }
        if (bannerState.value.isSuccess == null) {
            viewModel.getBanner()
        }
    }

    when {
        homeCategoryState.value.isLoading -> {

        }

        homeCategoryState.value.error != null -> {
            Toast.makeText(context, homeCategoryState.value.error, Toast.LENGTH_SHORT).show()
        }

        homeCategoryState.value.isSuccess != null -> {

            homeCategoryList.value = homeCategoryState.value.isSuccess!!

        }
    }

    when {
        bannerState.value.isLoading -> {
        }

        bannerState.value.error != null -> {
            Toast.makeText(context, bannerState.value.error, Toast.LENGTH_SHORT).show()
        }

        bannerState.value.isSuccess != null -> {
            bannerList.value = bannerState.value.isSuccess!!
        }
    }

    when {
        homeFlashSaleState.value.isLoading -> {
            AlertDialog(
                onDismissRequest = { /*TODO*/ },
                text = { CircularProgressIndicator() },
                confirmButton = { /*TODO*/ },
                dismissButton = { /*TODO*/ },
                modifier = Modifier
                    .clip(CircleShape)
                    .size(80.dp)
            )
        }

        homeFlashSaleState.value.error != null -> {
            Toast.makeText(context, homeFlashSaleState.value.error, Toast.LENGTH_SHORT).show()
        }

        homeFlashSaleState.value.isSuccess != null -> {
            homeFlashSaleList.value = homeFlashSaleState.value.isSuccess!!

            showUiState.value = true
        }
    }

    if (showUiState.value) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                SearchBox(
                    navController = navController,
                    modifier = modifier.padding(10.dp)
                )

                Spacer(modifier = modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = null,
                    tint = Color(0xFFF68B8B),
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(30.dp)
                        .clickable {
                            navController.navigate(Routes.NotificationScreen)
                        }
                )
            }

            Column() {


                Row(modifier.padding(start = 10.dp, end = 10.dp)) {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = modifier.weight(1f))
                    Text(
                        text = "See more",
                        color = Color(0xFFF68B8B),
                        modifier = modifier.clickable {
                            navController.navigate(Routes.AllCategoryScreen)

                        }

                    )

                }

                LazyRow(modifier = Modifier.heightIn(100.dp)) {
                    items(homeCategoryList.value.size) { index ->

                        CategoryItem(
                            homeCategoryList.value[index].categoryImageUrl,
                            homeCategoryList.value[index].categoryName,
                            navController
                        )

                    }
                }

                Box(
                    modifier = modifier
                        .height(220.dp)
                ) {

                    SliderBanner(
                        navController = navController,
                        bannerList = bannerList.value,
                    )

                }

                Row(modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp)) {
                    Text(
                        text = "Flash Sale",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = modifier.weight(1f))
                    Text(
                        text = "See more",
                        color = Color(0xFFF68B8B),
                        modifier = modifier.clickable {
                            navController.navigate(Routes.ProductByCategoryScreen(categoryName = "Flash Sale"))
                        }
                    )

                }
                LazyRow(modifier = Modifier.heightIn(100.dp)) {
                    items(homeFlashSaleList.value.size) { index ->
                        FlashSaleProductItem(
                            navController = navController,
                            homeFlashSaleList.value[index].productId,
                            homeFlashSaleList.value[index].productImageUrl,
                            homeFlashSaleList.value[index].productName,
                            homeFlashSaleList.value[index].productPrice,
                            homeFlashSaleList.value[index].discountedPrice,

                            )

                    }
                }

            }

        }
    }

}


@SuppressLint("DefaultLocale")
@Composable
fun FlashSaleProductItem(
    navController: NavController,
    productId: String,
    productImageUrl: String,
    productName: String,
    productPrice: String,
    discountedPrice: String
) {

    val productPriceFloat = productPrice.toFloatOrNull() ?: 0f
    val discountedPriceFloat = discountedPrice.toFloatOrNull() ?: 0f

    val off = if (productPriceFloat > 0) {
        (productPriceFloat - discountedPriceFloat) / productPriceFloat * 100
    } else {
        0f
    }
    val offString = String.format("%.1f", off)

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                navController.navigate(
                    Routes.ProductDetailsScreen(
                        productId = productId
                    )
                )

            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = productImageUrl,
            contentDescription = null,
            modifier = Modifier
                .height(150.dp)
                .width(150.dp)
                .border(
                    1.dp,
                    Color(0xFFF68B8B),
                    RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp)),
        //    contentScale = ContentScale.Crop

        )

        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    1.dp,
                    Color(0xFFF68B8B),
                    RoundedCornerShape(20.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .width(150.dp)
                    .padding(10.dp)
            ) {
                Text(
                    text = productName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Row {
                    Text(
                        text = "Rs. $productPrice",
                        textDecoration = TextDecoration.LineThrough,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "$offString% off",
                        color = Color(0xFFF68B8B),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = "Rs. $discountedPrice",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF68B8B)
                )
            }
        }
    }

}

@Composable
fun CategoryItem(categoryImageUrl: Any?, categoryName: Any?, navController: NavController) {

    Column(
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                navController.navigate(Routes.ProductByCategoryScreen(categoryName.toString()))

            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = categoryImageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(75.dp)
                .border(1.dp, Color(0xFFF68B8B), CircleShape)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(text = categoryName.toString())
    }

}

@Composable
fun SearchBox(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .background(Color(0xFFEBF5FC), shape = RoundedCornerShape(25.dp))
            .height(52.dp)
            .clickable {
                navController.navigate(Routes.SearchProductScreen)
            }
            .width(310.dp)
            .border(2.dp, Color(0xFFF68B8B), shape = RoundedCornerShape(25.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Black
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Search",
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

        }
    }
}


@ExperimentalPagerApi
@Composable
fun SliderBanner(
    navController: NavController,
    bannerList: List<ProductCategoryModel>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(initialPage = 0)

    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(2600)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % (pagerState.pageCount)
            )
        }
    }

    Column {
        HorizontalPager(
            count = bannerList.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 10.dp),
            modifier = modifier
                .height(180.dp)
                .fillMaxWidth()
        ) { page ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .clickable {
                        navController.navigate(Routes.ProductByCategoryScreen(bannerList[page].categoryName))

                    }
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                AsyncImage(
                    model = bannerList[page].categoryImageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)

        )
    }
}