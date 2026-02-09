package com.step.jdbc.runtime.txedit.provider.test.service;

import com.step.jdbc.runtime.txedit.model.LockParam;
import com.step.jdbc.runtime.txedit.model.TxInfo;
import com.step.jdbc.runtime.txedit.model.TxR;

public interface TxService {

    TxR beginEdit(LockParam txInfo);
    TxR updateData(TxInfo txInfo);

    TxR updateDataNonCommit(TxInfo txInfo);
    TxR exitEdit(TxInfo txInfo);



}
