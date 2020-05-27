package com.mjrj.lzh.pms.validate.validateclass;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.util
 * @Author: lzh
 * @CreateTime: 2020-03-17 23:25
 * @Description: ${Description}
 */
public class ValidationGroups {
    public interface Common{}

    public interface Register extends  Common{
    }

    public interface Forgot extends  Common{
    }

    public interface UpdateLeave extends  Common{
    }

    public interface SaveLeave extends  Common{
    }

    public interface SaveDept extends  Common{}
    public interface UpdateDept  extends  Common{}

    public interface  SavePosition extends Common{}
    public interface  UpdatePosition extends Common{}
    public interface  SaveCaigo extends Common{}
    public interface  UpdateCaigo extends Common{}

    public interface  UpdateUser extends Common{}
    public interface  AddUser extends Common{}
}
