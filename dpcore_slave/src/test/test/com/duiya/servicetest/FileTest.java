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
        backupService.aync( 1557244660109L, 1557244720104L);
    }
}
