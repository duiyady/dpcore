package com.duiya.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.util.Map;

public interface FileService {

    /**
     * 保存图片
     * @param account
     * @param multipartFiles
     * @return
     */
    Map<Integer, String> saveFile(String account, MultipartFile... multipartFiles);

    /**
     * 获取本机图片写入输出流
     * @param path
     * @param outputStream
     * @throws Exception
     */
    void getAndWriteFile(String path, ServletOutputStream outputStream) throws Exception;

    /**
     * 用于同步的时候自定义输出
     * @param path
     * @param outputStream
     */
    void getAndWriteFileL(ServletOutputStream outputStream, String path) throws Exception;

    void saveFile(MultipartFile... multipartFiles);

}
