package com.PixelUniverse.app.Service;

import lombok.AllArgsConstructor;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ImageService {
    private static final String API_KEY = "ea8dc596ecdc73d96850027fb1830c41"; // Thay đổi bằng API key của bạn
    private static final String UPLOAD_URL = "https://api.imgbb.com/1/upload";

    public String uploadImageToCloud(MultipartFile image) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Tạo request body với dữ liệu form
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", image.getOriginalFilename(),
                        RequestBody.create(MediaType.parse("image/*"), image.getBytes()))
                .addFormDataPart("key", API_KEY)
                .addFormDataPart("name", image.getOriginalFilename())
                .build();


        // Tạo yêu cầu POST
        Request request = new Request.Builder()
                .url(UPLOAD_URL)
                .post(requestBody)
                .build();

        // Gửi yêu cầu và xử lý phản hồi
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                // Xử lý phản hồi thành công ở đây
//                System.out.println("Upload successful: " + responseBody);
                JSONObject jsonObject = new JSONObject(responseBody);
                JSONObject data = jsonObject.getJSONObject("data");
                return data.getString("url");
            } else {
                // Xử lý lỗi ở đây
                System.out.println("Upload failed: " + response.code() + " " + response.message());
            }
            return null;
        }
    }
}
