package com.bntu.master.attendance.monitor.impl.resolver;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.entity.Base;

public interface Resolver<ENTITY extends Base> {

    ENTITY resolve(ObjectRef objectRef);

}
