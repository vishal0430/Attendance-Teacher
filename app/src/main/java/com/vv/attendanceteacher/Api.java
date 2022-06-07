package com.vv.attendanceteacher;

import com.vv.attendanceteacher.models.AttendanceStatusModel;
import com.vv.attendanceteacher.models.ClassDropdown;
import com.vv.attendanceteacher.models.DivisonDropDown;
import com.vv.attendanceteacher.models.LoginModel;
import com.vv.attendanceteacher.models.SubjectModel;

import org.json.JSONArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "http://3.110.3.252/Api/android_api.php/";
    @GET("?teacherlogin")
    Call<LoginModel> loginTeacher(@Query("email") String email, @Query("pass") String password);


    @GET("?class")
    Call<List<ClassDropdown>> getClassDropDown(@Query("teacher_id") String teacherId);

    @GET("?classdivision")
    Call<List<DivisonDropDown>> getDivisionDropDown(@Query("class_id") String classId);

    @GET("?subjects")
    Call<List<SubjectModel>> getSubjectDropDown(@Query("teacher_id") String teacherId);

    @FormUrlEncoded
    @POST("?takeattendance")
    Call<AttendanceStatusModel> takeAttendance(@Query("class_id") String classId,@Query("classdivision_id") String classdivsionId,@Query("date") String date,@Query("subject_id") String subjectId,@Field("studentId") JSONArray studentIds);
}