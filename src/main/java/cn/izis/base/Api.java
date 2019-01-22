package cn.izis.base;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.Map;

public interface Api {

    @POST("/api/game/user/editpasswd")
    Observable<Response<Map<String,String>>> modifyPassword(@Body Map<String,String> param);
}
