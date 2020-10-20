package com.bntu.master.attendance.monitor.impl.resolver;

import com.bntu.master.attendance.monitor.api.exception.NotFoundException;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.impl.dataaccess.RoleRepository;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class RoleResolver {

    @Autowired
    private RoleRepository repository;

    public Role resolve(RoleConstant role) {
        return resolve(ObjectRef.toObjectRef(role.getRole()));
    }

    public Role resolve(ObjectRef objectRef) {
        if (objectRef.isNullable()) {
            throw new NotFoundException();
        }
        return objectRef.isNullId() ? repository.findByName(objectRef.getQualifier()).orElseThrow(NotFoundException::new)
                : repository.findById(objectRef.getId()).orElseThrow(NotFoundException::new);
    }

    public Set<Role> resolveRefs(Collection<ObjectRef> roles) {
        List<Long> byId = new ArrayList<>();
        List<String> byQualifier = new ArrayList<>();
        for (ObjectRef r : roles) {
            if (r.getId()!=null) {
                byId.add(r.getId());
            } else {
                if (StringUtils.isNotBlank(r.getQualifier())){
                    byQualifier.add(r.getQualifier());
                }
            }
        }
        return repository.findAllByIdInOrNameIn(byId, byQualifier);
    }

}
