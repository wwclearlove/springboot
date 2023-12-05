package com.glasssix.dubbo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glasssix.dubbo.utils.TreeMenuNode;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestEncryDb {

    // 注入StringEncryptor
    @Autowired
    private StringEncryptor encryptor;

    @Test
    public void encry() {
        // 加密数据库用户名：testusername
        String username = encryptor.encrypt("wxc62dfc02c537bbe5");
        System.out.println(username);

        // 加密数据库密码：testpassword
        String password = encryptor.encrypt("69a45e39b4a44aca7089a94c07e85fb9");
        System.out.println(password);
    }

    @Test
    public void main() {

        String json = "[{\"childCount\":0,\"code\":\"data-manage\",\"ext\":\"data:bucket:query\",\"id\":2,\"menuType\":3,\"name\":\"是否可见\",\"parentId\":1,\"selectCount\":0,\"selected\":false,\"sort\":0},{\"childCount\":0,\"code\":\"model-library\",\"ext\":\"model:library:query\",\"id\":11,\"menuType\":3,\"name\":\"是否可见\",\"parentId\":10,\"selectCount\":0,\"selected\":false,\"sort\":0},{\"childCount\":0,\"code\":\"code-library\",\"ext\":\"code:library:query\",\"id\":21,\"menuType\":3,\"name\":\"是否可见\",\"parentId\":20,\"selectCount\":0,\"selected\":false,\"sort\":0},{\"childCount\":0,\"code\":\"task-manage\",\"ext\":\"task:query\",\"id\":31,\"menuType\":3,\"name\":\"是否可见\",\"parentId\":30,\"selectCount\":0,\"selected\":false,\"sort\":0},{\"childCount\":0,\"code\":\"user-manage-user\",\"ext\":\"auth:user:query\",\"id\":4011,\"menuType\":3,\"name\":\"是否可见\",\"parentId\":411,\"selectCount\":0,\"selected\":false,\"sort\":0},{\"childCount\":0,\"code\":\"user-manage-role\",\"ext\":\"auth:role:query\",\"id\":4012,\"menuType\":3,\"name\":\"是否可见\",\"parentId\":412,\"selectCount\":0,\"selected\":false,\"sort\":0},{\"childCount\":0,\"code\":\"operation-log\",\"ext\":\"auth:log:query\",\"id\":431,\"menuType\":3,\"name\":\"是否可见\",\"parentId\":43,\"selectCount\":0,\"selected\":false,\"sort\":0},{\"childCount\":0,\"code\":\"personal-center\",\"ext\":\"auth:personal:query\",\"id\":421,\"menuType\":3,\"name\":\"是否可见\",\"parentId\":42,\"selectCount\":0,\"selected\":false,\"sort\":0},{\"childCount\":0,\"code\":\"user-manage-role-create\",\"ext\":\"auth:role:add\",\"id\":4121,\"menuType\":3,\"name\":\"添加角色\",\"parentId\":412,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"code\":\"user-manage-role-disable\",\"ext\":\"auth:role:disable\",\"id\":4122,\"menuType\":3,\"name\":\"禁用角色\",\"parentId\":412,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"code\":\"user-manage-user-create\",\"ext\":\"auth:user:add\",\"id\":4013,\"menuType\":3,\"name\":\"添加用户\",\"parentId\":411,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"code\":\"user-manage-role-edit\",\"ext\":\"auth:role:edit\",\"id\":4123,\"menuType\":3,\"name\":\"编辑角色\",\"parentId\":412,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"code\":\"user-manage-user-disable\",\"ext\":\"auth:user:disable\",\"id\":4014,\"menuType\":3,\"name\":\"禁用用户\",\"parentId\":411,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"code\":\"user-manage-role-delete\",\"ext\":\"auth:role:delete\",\"id\":4124,\"menuType\":3,\"name\":\"删除角色\",\"parentId\":412,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"code\":\"operation-log-export\",\"ext\":\"auth:log:export\",\"id\":432,\"menuType\":3,\"name\":\"导出日志\",\"parentId\":43,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"code\":\"user-manage-user-edit\",\"ext\":\"auth:user:edit\",\"id\":4015,\"menuType\":3,\"name\":\"编辑用户\",\"parentId\":411,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"code\":\"user-manage-user-delete\",\"ext\":\"auth:user:delete\",\"id\":4016,\"menuType\":3,\"name\":\"删除用户\",\"parentId\":411,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"code\":\"personal-center-rename\",\"ext\":\"auth:personal:edit:name\",\"id\":422,\"menuType\":3,\"name\":\"修改姓名\",\"parentId\":42,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"code\":\"personal-center-phone\",\"ext\":\"auth:personal:edit:tel\",\"id\":424,\"menuType\":3,\"name\":\"修改手机\",\"parentId\":42,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"code\":\"personal-center-password\",\"ext\":\"auth:personal:edit:pwd\",\"id\":423,\"menuType\":3,\"name\":\"修改密码\",\"parentId\":42,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"code\":\"personal-center-email\",\"ext\":\"auth:personal:edit:mail\",\"id\":425,\"menuType\":3,\"name\":\"修改邮箱\",\"parentId\":42,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"icon\":\"ri-database-2-line\",\"id\":1,\"menuType\":1,\"name\":\"数据桶\",\"parentId\":0,\"selectCount\":0,\"selected\":false,\"sort\":1,\"url\":\"/data-manage\"},{\"childCount\":0,\"code\":\"user-manage-user\",\"id\":411,\"menuType\":2,\"name\":\"用户管理-用户\",\"parentId\":41,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"code\":\"user-manage-role\",\"id\":412,\"menuType\":2,\"name\":\"用户管理-角色\",\"parentId\":41,\"selectCount\":0,\"selected\":false,\"sort\":1},{\"childCount\":0,\"id\":41,\"menuType\":1,\"name\":\"用户管理\",\"parentId\":40,\"selectCount\":0,\"selected\":false,\"sort\":1,\"url\":\"/system-setting/user-manage\"},{\"childCount\":0,\"code\":\"model-library-create\",\"ext\":\"model:base:add\",\"id\":12,\"menuType\":3,\"name\":\"创建模型库\",\"parentId\":10,\"selectCount\":0,\"selected\":false,\"sort\":2},{\"childCount\":0,\"code\":\"model-library-rename\",\"ext\":\"model:base:rename\",\"id\":13,\"menuType\":3,\"name\":\"重命名模型库\",\"parentId\":10,\"selectCount\":0,\"selected\":false,\"sort\":2},{\"childCount\":0,\"code\":\"model-library-download\",\"ext\":\"model:library:download\",\"id\":14,\"menuType\":3,\"name\":\"下载模型库/模型文件 \",\"parentId\":10,\"selectCount\":0,\"selected\":false,\"sort\":2},{\"childCount\":0,\"code\":\"model-library-delete\",\"ext\":\"model:library:delete\",\"id\":15,\"menuType\":3,\"name\":\"删除模型库/模型文件\",\"parentId\":10,\"selectCount\":0,\"selected\":false,\"sort\":2},{\"childCount\":0,\"code\":\"model-library-upload\",\"ext\":\"model:info:add\",\"id\":16,\"menuType\":3,\"name\":\"上传模型文件\",\"parentId\":10,\"selectCount\":0,\"selected\":false,\"sort\":2},{\"childCount\":0,\"code\":\"model-library-edit\",\"ext\":\"model:info:edit\",\"id\":17,\"menuType\":3,\"name\":\"编辑模型文件\",\"parentId\":10,\"selectCount\":0,\"selected\":false,\"sort\":2},{\"childCount\":0,\"icon\":\"ri-share-line\",\"id\":10,\"menuType\":1,\"name\":\"模型库\",\"parentId\":0,\"selectCount\":0,\"selected\":false,\"sort\":2,\"url\":\"/model-manage\"},{\"childCount\":0,\"id\":42,\"menuType\":1,\"name\":\"个人中心\",\"parentId\":40,\"selectCount\":0,\"selected\":false,\"sort\":2,\"url\":\"/system-setting/personal-center\"},{\"childCount\":0,\"code\":\"code-library-create\",\"ext\":\"code:base:add\",\"id\":22,\"menuType\":3,\"name\":\"创建代码库\",\"parentId\":20,\"selectCount\":0,\"selected\":false,\"sort\":3},{\"childCount\":0,\"code\":\"code-library-rename\",\"ext\":\"code:base:rename\",\"id\":23,\"menuType\":3,\"name\":\"重命名代码库\",\"parentId\":20,\"selectCount\":0,\"selected\":false,\"sort\":3},{\"childCount\":0,\"code\":\"code-library-download\",\"ext\":\"code:library:download\",\"id\":24,\"menuType\":3,\"name\":\"下载代码库/代码文件 \",\"parentId\":20,\"selectCount\":0,\"selected\":false,\"sort\":3},{\"childCount\":0,\"code\":\"code-library-delete\",\"ext\":\"code:library:delete\",\"id\":25,\"menuType\":3,\"name\":\"删除代码库/代码文件\",\"parentId\":20,\"selectCount\":0,\"selected\":false,\"sort\":3},{\"childCount\":0,\"code\":\"code-library-upload\",\"ext\":\"code:info:add\",\"id\":26,\"menuType\":3,\"name\":\"上传代码文件\",\"parentId\":20,\"selectCount\":0,\"selected\":false,\"sort\":3},{\"childCount\":0,\"code\":\"code-library-edit\",\"ext\":\"code:info:edit\",\"id\":27,\"menuType\":3,\"name\":\"编辑代码文件\",\"parentId\":20,\"selectCount\":0,\"selected\":false,\"sort\":3},{\"childCount\":0,\"icon\":\"ri-terminal-box-line\",\"id\":20,\"menuType\":1,\"name\":\"代码库\",\"parentId\":0,\"selectCount\":0,\"selected\":false,\"sort\":3,\"url\":\"/code-manage\"},{\"childCount\":0,\"id\":43,\"menuType\":1,\"name\":\"操作日志\",\"parentId\":40,\"selectCount\":0,\"selected\":false,\"sort\":3,\"url\":\"/system-setting/operation-log\"},{\"childCount\":0,\"code\":\"task-manage-download\",\"ext\":\"task:download\",\"id\":33,\"menuType\":3,\"name\":\"下载运行结果\",\"parentId\":30,\"selectCount\":0,\"selected\":false,\"sort\":4},{\"childCount\":0,\"code\":\"task-manage-create\",\"ext\":\"task:add\",\"id\":32,\"menuType\":3,\"name\":\"创建任务\",\"parentId\":30,\"selectCount\":0,\"selected\":false,\"sort\":4},{\"childCount\":0,\"icon\":\"ri-file-code-line\",\"id\":30,\"menuType\":1,\"name\":\"任务\",\"parentId\":0,\"selectCount\":0,\"selected\":false,\"sort\":4,\"url\":\"/task\"},{\"childCount\":0,\"icon\":\"ri-settings-line\",\"id\":40,\"menuType\":1,\"name\":\"系统设置\",\"parentId\":0,\"selectCount\":0,\"selected\":false,\"sort\":5,\"url\":\"/system-setting\"}]";
        List<TreeMenuNode> TreeMenuNodeS = JSONObject.parseArray(json, TreeMenuNode.class);
//        System.out.println(TreeMenuNodeS);
        List<TreeMenuNode> TreeMenuNodeS1 = buildTree(TreeMenuNodeS);
        System.out.println(JSON.toJSONString(TreeMenuNodeS1));

    }

    private List<TreeMenuNode> buildTree(List<TreeMenuNode> nodes) {
        Map<Long, List<TreeMenuNode>> children = nodes.stream().filter(node -> node.getParentId() != 0L)
                .collect(Collectors.groupingBy(node -> node.getParentId()));

        Map<Long, List<TreeMenuNode>> children2 = nodes.stream()
                .collect(Collectors.groupingBy(node -> node.getParentId()));

        nodes.forEach(node -> {
                    List<TreeMenuNode> treeMenuNodes = children.get(node.getId());
                    node.setChildList(treeMenuNodes);
                    if (!CollectionUtils.isEmpty(treeMenuNodes)) {
                        node.setChildIds(treeMenuNodes.stream().map(TreeMenuNode::getId).collect(Collectors.toSet()));
                    }
                }
        );
        return nodes.stream().
                filter(node -> node.getParentId() == 0).
                collect(Collectors.toList());
    }
}
