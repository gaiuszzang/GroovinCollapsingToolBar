## GroovinCollapsingToolBar
This library offers a Collapsing Tool Bar Layout for Jetpack Compose.

![groovin_collapsing_example_main](https://user-images.githubusercontent.com/15318053/208280206-b9866b75-5eeb-4d1d-b4a2-2e1bba35a3a6.gif)

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
    implementation 'com.github.gaiuszzang:GroovinCollapsingToolBar:1.0.0'
}
```


## Usage
### CollapsingToolBarLayout
`CollapsingToolBarLayout` is Composable Scaffold Layout that has a Top bar & content.
```kotlin
val collapsingToolBarState = rememberCollapsingToolBarState(
    toolBarMaxHeight = 200.dp,
    toolBarMinHeight = 56.dp,
    collapsingOption = CollapsingOption.EnterAlwaysCollapsed
)
CollapsingToolBarLayout(
    state = collapsingToolBarState,
    autoSnapOption = AutoSnapOption.NoAutoSnap,
    toolbar = { toolBarCollapsedInfo ->
        //TODO : Top Bar
        TopBarLayout(
            modifier = Modifier
                .height(toolBarCollapsedInfo.toolBarHeight)
        )
    }
) { innerPadding ->
    //TODO : Content
    ContentLayout(
        modifier = Modifier
            .padding(innerPadding)
    )
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

#### AutoSnapOption
AutoSnap means that Top Bar automatically expands or collapses when scrolling is stopped.
 - AutoSnapOption.NoAutoSnap `default`
 - AutoSnapOption.AutoSnapWithScrollableState(scrollableState)

For use AutoSnapWithScrollableState, You need to pass the inner scrollableState. you can follow as the example below :
```kotlin
val innerScrollState = rememberLazyListState()
CollapsingToolBarLayout(
    state = rememberCollapsingToolBarState(200.dp, 56.dp),
    autoSnapOption = AutoSnapOption.AutoSnapWithScrollableState(innerScrollState),
    toolbar = { toolBarCollapsedInfo ->
        //ToolBar Layout
    }
) { innerPadding ->
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding,
        state = innerScrollState
    ) {
        items(contentList) { item ->
            Menu(item)
        }
    }
}
```

#### ToolBarCollapsedInfo
`ToolBarCollapsedInfo` is Top Bar Status class that includes Top Bar's height & progress information.
 - height : You need to use this value for updating Top Bar's height.
 - progress : This Float value is range in 0 ~ 1. 0 when Top bar is fully expanded, and 1 when fully collapsed.
You can follow as the example below :
```kotlin
CollapsingToolBarLayout(
    state = rememberCollapsingToolBarState(200.dp, 56.dp),
    toolbar = { toolBarCollapsedInfo ->
        // Note : pass the progress parameter to MotionTopBar for updating MotionLayout progress status.
        MotionTopBar(
            modifier = Modifier
                .height(toolBarCollapsedInfo.toolBarHeight),
            progress = toolBarCollapsedInfo.progress
        )
    }
)
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
