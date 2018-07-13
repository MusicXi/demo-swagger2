package hello.controller;

import hello.bean.Greeting;
import hello.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

/**
 * restful测试类
 * @author linrx1
 *
 */
@Api(value = "/", tags = "swagger2 Demo接口")
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @ApiOperation(value="遇见用户get", notes="get提交用户名")
    @RequestMapping(value="/greeting" ,method= RequestMethod.GET)
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name, HttpSession session) {
    	// 模拟用户登入
    	User user = new User();
    	user.setUsername(name);
    	user.setId(12345L);
    	session.setAttribute("user", user);
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }

    @ApiOperation(value="遇见用户post", notes="使用请求体提交用户信息,测试post日志参数记录")
    @RequestMapping(value="/greetingByJson" ,method= RequestMethod.POST)
    public Greeting greetingByJson(@RequestBody User user, HttpSession session) {
        // 模拟用户登入
        user.setId(12345L);
        return new Greeting(counter.incrementAndGet(),
                String.format(template, user.getName()));
    }
}