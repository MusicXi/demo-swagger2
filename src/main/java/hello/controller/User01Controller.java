package hello.controller;

import hello.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "用户操作接口v1")
@RestController
@RequestMapping(value="/v1/users")
public class User01Controller {

    static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

    @ApiOperation(value="用户列表", notes="对该方法的备注信息说明")
    @GetMapping
    public List<User> getUserList(HttpServletRequest request) {
         List<User> r = new ArrayList<User>(users.values());
        return r;
    }

    @ApiOperation(value="用户创建", notes="根据User对象创建用户")
    @PostMapping
    public String postUser(@ApiParam("用户详细实体user")@RequestBody User user) {
        users.put(user.getId(), user);
        return "success";
    }

    @ApiOperation(value="用户详情", notes="根据url的id来获取用户详细信息")
    @GetMapping("/{id}")
    public User getUser(@ApiParam("用户ID") @PathVariable Long id) {
        return users.get(id);
    }

    @ApiOperation(value="用户修改", notes="根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @PutMapping("/{id}")
    public String putUser(@ApiParam("用户ID") @PathVariable Long id, @RequestBody User user) {
        User u = users.get(id);
        u.setName(user.getName());
        //u.setAge(user.getAge());
        users.put(id, u);
        return "success";
    }
    

    @ApiOperation(value="用户删除", notes="根据url的id来指定删除对象")
    @DeleteMapping("/{id}")
    public String deleteUser(@ApiParam("用户ID") @PathVariable Long id) {
        users.remove(id);
        return "success";
    }

}