# RichText
## 图文混排
## 使用方法：
add dependency :

## Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
## Step 2. Add the dependency
```
dependencies {
  implementation 'com.github.HeyGanhana:RichText:Tag'
}
```
### xml文件中调用
```
<com.heyganhana.richtext.RichText
    android:id="@+id/rich_text"
    android:layout_height="wrap_content"
    android:layout_width="match_parent">
</com.heyganhana.richtext.RichText>

```
添加图片：

> 异步加载图片，加载过程中动画加载友好交互，加载失败则显示为error Icon。

```
mRichText.addImage(Uri);
mRichText.addImage(File);
mRichText.addImage(String path);
获取整个TextString：
mRichText.getText();
图片在文字中的格式为：<img src="文件的uri"/>
```
