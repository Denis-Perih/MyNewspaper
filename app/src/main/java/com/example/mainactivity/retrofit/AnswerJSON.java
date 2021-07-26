package com.example.mainactivity.retrofit;

import java.util.List;

public interface AnswerJSON {

    void onSuccessJSON(List<Post> posts);

    void onFailureJSON();
}
