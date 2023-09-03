## GroovinCollapsingToolBar
[![Release](https://jitpack.io/v/gaiuszzang/GroovinCollapsingToolBar.svg)](https://jitpack.io/#gaiuszzang/GroovinCollapsingToolBar)  
This library offers a Collapsing Tool Bar Layout for Jetpack Compose.

![sample_main](https://github.com/gaiuszzang/GroovinCollapsingToolBar/assets/15318053/78f030b5-a416-4cfc-9b33-253f3b51ab9e)

## Including in your project
### Gradle
Add below codes to `settings.gradle`.
```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
for old gradle version, Add below codes to **your project**'s `build.gradle`.
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

And add a dependency code to your **module**'s `build.gradle` file.
```gradle
dependencies {
    implementation 'com.github.gaiuszzang:GroovinCollapsingToolBar:x.x.x'
}
```


## Usage
### CollapsingToolBarLayout
`CollapsingToolBarLayout` is Composable Scaffold Layout that contains ToolBar & Content.
```kotlin
CollapsingToolBarLayout(
    state = rememberCollapsingToolBarState(200.dp, 56.dp),
    updateToolBarHeightManually = false, //Optional, default false
    toolbar = {
        //in CollapsingToolBarLayoutToolBarScope
        ToolBar(...) //Tool Bar Composable
    }
) {
    //in CollapsingToolBarLayoutContentScope
    Content(...) //Content Composable
}
```

#### CollapsingToolBarState
CollapsingToolBar needs `CollapsingToolBarState` instance for store and use its status.
```kotlin
val collapsingToolBarState = rememberCollapsingToolBarState(
    toolBarMaxHeight = 200.dp,
    toolBarMinHeight = 56.dp,
    collapsingOption = CollapsingOption.EnterAlwaysCollapsed
)
```
You need to define ToolBar's Min/Max Height. also, You can define the collapsing Options.
 - CollapsingOption.EnterAlways
 - CollapsingOption.EnterAlwaysCollapsed `default`
 - CollapsingOption.EnterAlwaysAutoSnap
 - CollapsingOption.EnterAlwaysCollapsedAutoSnap

|                                                          EnterAlways                                                          |                                                          EnterAlwaysCollapsed                                                          |
|:-----------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------:|
|     ![enterAlways](https://user-images.githubusercontent.com/15318053/210083650-d1ed6547-722b-4a96-ba7c-5da482576019.gif)     |     ![EnterAlwaysCollapsed](https://user-images.githubusercontent.com/15318053/210083690-2ce4647c-1559-4394-9f4c-5d09c67de522.gif)     |
|                                                    **EnterAlwaysAutoSnap**                                                    |                                                    **EnterAlwaysCollapsedAutoSnap**                                                    |
| ![EnterAlwaysAutoSnap](https://user-images.githubusercontent.com/15318053/210083656-62fb23a3-a720-405e-b6b9-393c4c570012.gif) | ![enterAlwaysCollpasedAutoSnap](https://user-images.githubusercontent.com/15318053/210083692-f8d4e4ab-b36f-4f9c-ba38-b794ee064163.gif) |

>AutoSnap means that Tool Bar automatically expands or collapses when scrolling is stopped.


#### updateToolBarHeightManually
The height of the toolbar is basically determined by internal logic, but you can disable this feature by setting this parameter to false.
 - false : The height of the toolbar is determined by internal logic. `default`
 - true : You must setting the height of toolbar manually.


#### CollapsingToolBarLayoutToolBarScope
A `CollapsingToolBarLayoutToolBarScope` provides a scope for the tool bar of CollapsingToolBarLayout.
This scope provides following member variables and kotlin extensions.
 - collapsedInfo : ToolBarCollapsedInfo
   - `ToolBarCollapsedInfo` is Tool Bar Status class that includes Tool Bar's height & progress information.
   - collapsedInfo.height : You need to use this value for updating Tool Bar's height.
   - collapsedInfo.progress : This Float value is range in 0 ~ 1. 0 when Tool bar is fully expanded, and 1 when fully collapsed.
   - You can follow as the example below :
     ```kotlin
     CollapsingToolBarLayout(
         state = rememberCollapsingToolBarState(200.dp, 56.dp),
         toolbar = {
             MotionTopBar(
                 progress = collapsedInfo.progress
             )
         }
     )
     ```
 - Modifier.toolBarScrollable()
   - This function supports to applying scrollable gesture in ToolBar.

     |                                                                default                                                               |                                                         toolBarScrollable                                                         |
     |:------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------:|
     | ![toolBarNonScrollable](https://github.com/gaiuszzang/GroovinCollapsingToolBar/assets/15318053/71894902-6a02-4643-bc4a-28241a8b3780) | ![toolBarScrollable](https://github.com/gaiuszzang/GroovinCollapsingToolBar/assets/15318053/6b264e5f-b5b0-41d5-8924-5390995f500f) |
 - Modifier.requiredToolBarMaxHeight()
   - This function supports that ToolBar appears to scroll with fixed size.

     |                                                                default                                                                |                                                      requiredToolBarMaxHeight                                                      |
     |:-------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------:|
     | ![toolBarNonFixedHeight](https://github.com/gaiuszzang/GroovinCollapsingToolBar/assets/15318053/1a9d5e04-eb84-44cb-aa86-2206180ad4f9) | ![toolBarFixedHeight](https://github.com/gaiuszzang/GroovinCollapsingToolBar/assets/15318053/c2f4391d-c90b-4630-9a9a-5bb3bc4c9911) |

  
#### CollapsingToolBarLayoutContentScope
A `CollapsingToolBarLayoutContentScope` provides a scope for the content of CollapsingToolBarLayout.
Also, following kotlin extension methods are provided for scrolling contents with ScrollableState or LazyListState.
 - ScrollableState.scrollWithToolBarBy()
 - ScrollableState.animateScrollWithToolBarBy()
 - LazyListState.animateScrollWithToolBarToItem()

You can follow as the example below :
```kotlin
val lazyListState = rememberLazyListState()

CollapsingToolBarLayout(
    state = rememberCollapsingToolBarState(200.dp, 56.dp),
    toolbar = { toolBarCollapsedInfo ->
        TopBar(...)
    }
) {
    LazyColumn(
        state = lazyListState
    ) {
        items(contentList) {
            Item(it)
        }
    }
    FloatingButton(
        onClick = {
            scope.launch {
                // Scroll to top
                lazyListState.animateScrollWithToolBarToItem(0)
            }
        }
    )
}
```
  
  

## License
```xml
Copyright 2022 gaiuszzang (Mincheol Shin)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
