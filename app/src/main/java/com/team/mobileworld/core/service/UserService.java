package com.team.mobileworld.core.service;

import com.team.mobileworld.core.object.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {

    public static final String URL_GET_INFO = "api/user/find/";

    public static final String URL_PUT_INFO = "api/user";

    public static final String URL_PUT_IMAGE = "api/user/image";

    public static final String URL_CHANGE_PASSWORD = "api/user/password";

    public static final String URL_FORGOT_PASSWORD = "api/login/forgot";

    public static final String URL_LINKED_ACCOUNT = "api/linked";


    /**
     * Tải thông tin tài khoản của người dùng
     * Nhận: Đối tượng User chứa thông tin khách hàng
     *
     * @param username
     * @param id
     * @return
     */
    @GET(URL_GET_INFO)
    Call<User> getPersonalInfo(@Header("x-access-token") String token);


    /**
     * Cập nhật thông tin tài khoản
     * Đối tượng trả về 1 mã xác nhận
     */
    @PUT(URL_PUT_INFO)
    Call<User> updatePersonalInfo(@Header("x-access-token") String token, @Body User user);

    /**
     * Cập nhật ảnh của người dùng trên server
     * Ảnh có thể là: Background hoặc Image
     *
     * @param response
     * @return
     */
    @Multipart
    @PUT(URL_PUT_IMAGE)
    Call<ResponseBody> updateImage(
            @Header("x-access-token") String accesstoken
            , @Part MultipartBody.Part image
            , @Part("fileinfo") RequestBody request
    );

    /**
     * Thay doi tai khoan mat khau cua nguoi dung
     * giu di: id user, mat khau cu, mat khau moi
     * nhan ve: 1 ma xac nhan thay doi mat khau thanh cong hoac 1 thong bao loi
     * giử lời nhắn thay đổi mật khẩu về email
     */
    @FormUrlEncoded
    @PUT("api/user/password")
    Call<ResponseBody> changePassowrd(
            @Header("x-access-token") String token
            , @Field("oldpassword") String oldpassword
            , @Field("newpassword") String newpassword
    );


    /**
     * Giử một yêu cầu lấy lại mật khẩu người dùng
     * Giử đi: một 1 username, và email đăng ký tài khoản
     * Nhận về: giử mật khẩu về email đăng ký tài khoản đó
     */
    @FormUrlEncoded
    @POST(URL_FORGOT_PASSWORD)
    Call<ResponseBody> forgotPassword(
            @Field("username") String username
            , @Field("email") String email);


    /**
     * Giử một id facebook và 1 id username lên server
     * Server: xác nhận liên kết giữa facebook và tài khoản người dùng
     * Server sẽ trả về 1 mã xác nhận tài khoản liên kết
     */
    @FormUrlEncoded
    @POST(URL_LINKED_ACCOUNT)
    Call<ResponseBody> linkAccountFacebook(@Field("id") long id, @Field("idfacebook") long idfacekook);

}
