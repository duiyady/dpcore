package com.duiya.servicetest;

import com.duiya.config.DataConfig;
import com.duiya.config.SpringMvcConfig;
import com.duiya.config.TaskExecutorConfig;
import com.duiya.dao.FileDao;
import com.duiya.service.BackupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringMvcConfig.class, DataConfig.class, TaskExecutorConfig.class})
@WebAppConfiguration
public class FileTest {

    @Autowired
    private BackupService backupService;

    @Autowired
    private FileDao fileDao;
    @Test
    public void fileTest(){
        Long now = new Date().getTime();
        Long la = new Long("1547976210216");
        //backupService.aync(la, now);

        List<String> list = fileDao.getLocalRecentFile(la, now,"156800");


    }
}
