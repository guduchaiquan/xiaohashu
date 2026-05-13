package com.quanxiaoha.xiaohashu.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.google.common.base.Preconditions;
import com.quanxiaoha.framework.common.exception.BizException;
import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.framework.common.util.JsonUtils;
import com.quanxiaoha.xiaohashu.auth.constant.RedisKeyConstants;
import com.quanxiaoha.xiaohashu.auth.enums.LoginTypeEnum;
import com.quanxiaoha.xiaohashu.auth.enums.ResponseCodeEnum;
import com.quanxiaoha.xiaohashu.auth.model.vo.user.UpdatePasswordReqVO;
import com.quanxiaoha.xiaohashu.auth.model.vo.user.UserLoginReqVO;
import com.quanxiaoha.xiaohashu.auth.rpc.UserRpcService;
import com.quanxiaoha.xiaohashu.auth.service.AuthService;
import com.quanxiaoha.xiaohashu.user.dto.resp.FindUserByPhoneRspDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserRpcService userRpcService;
    /**
     * 登录与注册
     *
     * @param userLoginReqVO
     * @return
     */
    @Override
    public Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO) {
        String phone = userLoginReqVO.getPhone();
        Integer type = userLoginReqVO.getType();
        Long userId = null;

        LoginTypeEnum loginTypeEnum = LoginTypeEnum.valueOf(type);

        if (Objects.isNull(loginTypeEnum)) {
            throw new BizException(ResponseCodeEnum.LOGIN_TYPE_ERROR);
        }

        // 判断登录类型
        switch (loginTypeEnum) {
            case VERIFICATION_CODE: // 验证码登录
                String verificationCode = userLoginReqVO.getCode();

                // 校验入参验证码是否为空
                Preconditions.checkArgument(StringUtils.isNotBlank(verificationCode), "验证码不能为空");

                // 构建验证码 Redis Key
                String key = RedisKeyConstants.buildVerificationCodeKey(phone);
                // 查询存储在 Redis 中该用户的登录验证码
                String sentCode = (String) redisTemplate.opsForValue().get(key);

                if (StringUtils.isBlank(sentCode)) {
                    throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }

                // 判断用户提交的验证码，与 Redis 中的验证码是否一致
                if (!StringUtils.equals(verificationCode, sentCode)) {
                    throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }

                // RPC: 调用用户服务，通过手机号查询用户
                FindUserByPhoneRspDTO findUserByPhoneRspDTO = userRpcService.findUserByPhone(phone);

                log.info("==> 用户是否注册, phone: {}, userDO: {}", phone, JsonUtils.toJsonString(findUserByPhoneRspDTO));

                // 判断是否注册
                if (Objects.isNull(findUserByPhoneRspDTO)) {
                    // 若此用户还没有注册，系统自动注册该用户
                    userId = userRpcService.registerUser(phone);
                } else {
                    // 已注册，则获取其用户 ID
                    userId = findUserByPhoneRspDTO.getId();
                }
                break;
            case PASSWORD: // 密码登录
                String password = userLoginReqVO.getPassword();

                // RPC: 调用用户服务，通过手机号查询用户
                FindUserByPhoneRspDTO userByPhone = userRpcService.findUserByPhone(phone);

                // 判断该手机号是否注册
                if (Objects.isNull(userByPhone)) {
                    throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
                }

                // 密码必须填写
                Preconditions.checkArgument(StringUtils.isNotBlank(password), "密码不能为空");

                // 拿到密文密码
                String encodePassword = userByPhone.getPassword();

                log.info("==> 密码验证 - 用户手机号: {}", phone);

                // 匹配密码是否一致
                boolean isPasswordCorrect = false;

                // 检查数据库密码是否为 BCrypt 格式
                if (encodePassword != null && (encodePassword.startsWith("$2a$") || encodePassword.startsWith("$2b$") || encodePassword.startsWith("$2y$"))) {
                    // BCrypt 格式，使用 BCrypt 验证
                    isPasswordCorrect = passwordEncoder.matches(password, encodePassword);
                } else {
                    // 非 BCrypt 格式，使用明文比较（兼容旧数据）
                    log.info("==> 数据库密码非 BCrypt 格式，使用明文比较");
                    isPasswordCorrect = password.equals(encodePassword);
                }

                // 如果不正确，则抛出业务异常，提示用户名或者密码不正确
                if (!isPasswordCorrect) {
                    throw new BizException(ResponseCodeEnum.PHONE_OR_PASSWORD_ERROR);
                }

                userId = userByPhone.getId();
                break;
            default:
                break;
        }

        // SaToken 登录用户, 入参为用户 ID
        StpUtil.login(userId);

        // 获取 Token 令牌
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 返回 Token 令牌
        return Response.success(tokenInfo.tokenValue);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public Response<?> logout() {
        // 退出登录 (调用无参方法直接退出当前会话)
        StpUtil.logout();

        return Response.success();
    }

    /**
     * 修改密码
     *
     * @param updatePasswordReqVO
     * @return
     */
    @Override
    public Response<?> updatePassword(UpdatePasswordReqVO updatePasswordReqVO) {
        // 新密码
        String newPassword = updatePasswordReqVO.getNewPassword();
        // 密码加密
        String encodePassword = passwordEncoder.encode(newPassword);

        // RPC: 调用用户服务：更新密码
        userRpcService.updatePassword(encodePassword);

        return Response.success();
    }



}
