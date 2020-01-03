# StoryView
The library that makes horizontal progress view like Instagram stories. You can use in images or videos.

<a href="https://imgflip.com/gif/3kbkj9"><img src="https://i.imgflip.com/3kbkj9.gif" title="made at imgflip.com"/></a>
<a href="https://imgflip.com/gif/3kbn4p"><img src="https://i.imgflip.com/3kbn4p.gif" title="made at imgflip.com"/></a>

## How to use

### In XML

```
  <com.felix.storyview.StoryView
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      .
      .
      app:storyCount="5"
      app:storyBackgroundColor="@android:color/darker_gray"
      app:storyFrontColor="@android:color/white"
      app:duration="3000"
      app:storyHeight="5dp"
      app:autoNextStory="true"
      .
      .
      android:id="@+id/story_view"
      android:layout_marginTop="32dp"
      />
```
&nbsp;&nbsp; You can add this attributes in XML. This values is default.
### In Code
```
    StoryView storyView = findViewById(R.id.story_view);
    storyView.addStoryListener(this);
    storyView.start();
```
&nbsp;&nbsp; After calling start(), stories will start loading and you should add story listener like this.
```
    @Override
    public void onCurrentStory(int currentStory) {
        Log.d(TAG,"onCurrentStory: " + currentStory);
    }
```
&nbsp;&nbsp; onCurrentStory will be called before current story start.
```
    @Override
    public void onCompleteStories() {
        Log.d(TAG,"onCompleteStories");
    }
```
&nbsp;&nbsp; onCompleteStories will be called after all stories end.    

You can also use this library in videos, if the follows these instructions.  
&nbsp;&nbsp; - You must set `app:autoNextStory="false"`. This attribute provides the stories to stop the next story.  
&nbsp;&nbsp; - When the next video ready, VideoPlayer will give you the duration of the video. `storyView.setDuration(7587);` call this function with video's duration then call `storyView.start();` to start next story.    
  
I made a simple example for image and videos. You should check the repository.  
  
### More
&nbsp;&nbsp; `storyView.stop();` stop current story view.  
&nbsp;&nbsp; `storyView.previousStory();` go back to previous story view.  
&nbsp;&nbsp; `storyView.nextStory();` go to next story view.    
&nbsp;&nbsp; `storyView.setAllStoriesMin();` will set all stories undone.  
&nbsp;&nbsp; `storyView.setStoryCount(8);` will set new number of story.   
&nbsp;&nbsp; `storyView.setDuration(5000);` will set new duration of story(5 sec).  




