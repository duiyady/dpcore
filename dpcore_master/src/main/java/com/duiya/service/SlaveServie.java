package com.duiya.service;

import com.duiya.init.BaseConfig;
import com.duiya.init.SlaveMess;
import org.springframework.stereotype.Service;

@Service
public class SlaveServie {
    public int changeSlaveState(int state, String iphash6){
        int result = SlaveMess.changestate(state, iphash6);
        return result;
    }

    public void changeblancetype(Integer blance) {
        BaseConfig.BALANCEFUN = blance;
        if(blance == 1) {
            SlaveMess.initList();
        }
    }

    public int changeQuan(int quan, String iphash6) {
        return SlaveMess.changeQuan(quan, iphash6);
    }
}
