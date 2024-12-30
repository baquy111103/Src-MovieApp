package cms.cms.common;

import cms.cms.dto.AjaxSearchDto;
import io.micrometer.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Log4j2
public class Helper {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    // Hàm chuyển đổi từ List<String[]> thành List<AjaxSearchDto>
    public static List<AjaxSearchDto> listAjax(List<Object[]> input, int type) {
        List<AjaxSearchDto> listAjax = new ArrayList<>();
        for (Object[] strings : input) {
            AjaxSearchDto dto = new AjaxSearchDto();
            dto.setId((String) strings[0]);
            if (type == 0) {
                dto.setText(strings[0] + " - " + strings[1]);  // category_code - name
            } else {
                dto.setText((String) strings[1]);
            }
            listAjax.add(dto);
        }
        return listAjax;
    }

    // Hàm xử lý chuỗi tìm kiếm
    public static String processStringSearch(String input) {
        if (StringUtils.isNotEmpty(input)) {
            return input.trim();  // Loại bỏ khoảng trắng đầu và cuối
        }
        return "";
    }


    public static Date standardDateV2(String input) {
        if (input.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
            input += ":00";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        try {
            LocalDateTime dateTime = LocalDateTime.parse(input, formatter);
            Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
            return Date.from(instant);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Chuỗi thời gian không hợp lệ: " + input);
        }
    }

    public static String generateCode(String code) throws NoSuchAlgorithmException {
        if (StringUtils.isEmpty(code)) {
            throw new IllegalArgumentException("Tên không được để trống");
        }

        String prefix = code.trim().toUpperCase().replaceAll("[^A-Z]", "");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(prefix.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(String.format("%02x", hashBytes[i]));
        }

        return prefix + "-" + sb.toString().toUpperCase();
    }
}
