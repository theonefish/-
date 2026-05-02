<template>
  <div class="login-page">
    <!-- 顶部导航栏 -->
    <header class="login-header">
      <div class="header-left">
        <el-icon size="24" color="#003c90"><FirstAidKit /></el-icon>
        <span class="header-title">康华医院管理系统</span>
      </div>
      <div class="header-right">
        <el-icon size="18" color="#64748b"><InfoFilled /></el-icon>
        <span>SECURE ACCESS</span>
      </div>
    </header>

    <!-- 主体内容区 - 左右分栏布局 -->
    <main class="login-main">
      <!-- 左侧品牌展示区 -->
      <section class="login-brand">
        <div class="brand-content">
          <h1 class="brand-title">康华医院管理系统</h1>
          <p class="brand-desc">
            Efficient patient flow management and<br />
            secure administrative controls for<br />
            healthcare professionals.
          </p>
        </div>
        <div class="brand-footer">
          <el-icon size="20"><Lock /></el-icon>
          <span>ENCRYPTION STANDARD AES-256</span>
        </div>
      </section>

      <!-- 右侧登录表单区 -->
      <section class="login-form-section">
        <div class="form-wrapper">
          <h2 class="form-title">Hospital Login</h2>
          <p class="form-subtitle">
            Please enter your credentials to access the portal.
          </p>

          <el-form :model="form" :rules="rules" ref="formRef" class="login-form">
            <!-- 用户名/手机号输入框 -->
            <el-form-item prop="username" class="form-item-custom">
              <label class="input-label">PHONE NUMBER OR USERNAME</label>
              <el-input
                v-model="form.username"
                placeholder="Enter your registration ID"
                size="large"
              >
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <!-- 密码输入框 -->
            <el-form-item prop="password" class="form-item-custom">
              <label class="input-label">PASSWORD</label>
              <el-input
                v-model="form.password"
                type="password"
                placeholder="Enter your password"
                size="large"
                show-password
                @keyup.enter="handleLogin"
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <!-- 验证码输入框 -->
            <el-form-item prop="captcha" class="form-item-custom captcha-row">
              <label class="input-label">VERIFICATION CODE</label>
              <div class="captcha-group">
                <el-input
                  v-model="form.captcha"
                  placeholder="CAPTCHA"
                  size="large"
                  @keyup.enter="handleLogin"
                >
                  <template #prefix>
                    <el-icon><Key /></el-icon>
                  </template>
                </el-input>
                <div class="captcha-display" @click="refreshCaptcha">
                  <span>{{ captchaCode }}</span>
                </div>
                <el-button
                  class="captcha-refresh-btn"
                  :icon="RefreshRight"
                  circle
                  size="large"
                  @click="refreshCaptcha"
                />
              </div>
            </el-form-item>

            <!-- 记住我 & 忘记密码 -->
            <div class="form-options">
              <el-checkbox v-model="form.rememberMe" class="remember-me">
                Remember Me
              </el-checkbox>
              <a href="javascript:;" class="forgot-link">Forgot Password?</a>
            </div>

            <!-- 登录按钮 -->
            <el-form-item>
              <el-button
                type="primary"
                size="large"
                class="login-btn"
                :loading="loading"
                @click="handleLogin"
              >
                Login to Dashboard
              </el-button>
            </el-form-item>
          </el-form>

          <p class="security-notice">
            Authorized Personnel Only. All activities are logged.
          </p>
        </div>
      </section>
    </main>

    <!-- 底部版权信息 -->
    <footer class="login-footer">
      <span>&copy; 2024 康华医院管理系统. SECURE MEDICAL ENVIRONMENT.</span>
      <span>&copy; 2024 康华医院管理系统. SECURE MEDICAL ENVIRONMENT.</span>
      <span>&copy; 2024 康华医院管理系统. SECURE MEDICAL ENVIRONMENT.</span>
      <span>&copy; 2024 康华医院管理系统. SECURE MEDICAL ENVIRONMENT.</span>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  User, Lock, Key, FirstAidKit,
  InfoFilled, RefreshRight
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { login, getUserInfo, getMenus } from '@/api/auth'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)
const captchaCode = ref('')

const form = reactive({
  username: 'admin',
  password: '123456',
  captcha: '',
  rememberMe: false
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (value.toLowerCase() !== captchaCode.value.toLowerCase()) {
          callback(new Error('验证码错误'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 生成随机4位验证码（大写字母+数字）
const generateCaptcha = () => {
  const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789'
  let code = ''
  for (let i = 0; i < 4; i++) {
    code += chars[Math.floor(Math.random() * chars.length)]
  }
  captchaCode.value = code
}

// 刷新验证码并清空输入
const refreshCaptcha = () => {
  generateCaptcha()
  form.captcha = ''
}

// 处理登录请求
const handleLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    // 调用后端登录接口获取token
    const res = await login({
      username: form.username,
      password: form.password
    }) as any
    userStore.setToken(res.token)
    // 获取用户信息和菜单权限
    const userRes = await getUserInfo() as any
    userStore.setUserInfo(userRes)
    const menuRes = await getMenus() as any
    userStore.setMenus(menuRes)
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    refreshCaptcha()
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  generateCaptcha()
})
</script>

<style scoped lang="scss">
/* ========== 全局容器样式 ========== */
.login-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f7f9fb;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
}

/* ========== 顶部导航栏 ========== */
.login-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 40px;
  background: #ffffff;
  border-bottom: 1px solid #e0e3e5;

  .header-left {
    display: flex;
    align-items: center;
    gap: 10px;

    .header-title {
      font-size: 16px;
      font-weight: 700;
      color: #003c90;
      letter-spacing: 0.02em;
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 12px;
    font-weight: 600;
    color: #64748b;
    letter-spacing: 0.08em;
  }
}

/* ========== 主体内容区 ========== */
.login-main {
  flex: 1;
  display: flex;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 24px rgba(15, 82, 186, 0.08);
  margin-top: 32px;
  margin-bottom: 32px;
}

/* ========== 左侧品牌展示区 ========== */
.login-brand {
  width: 480px;
  min-width: 480px;
  background: linear-gradient(135deg, #003c90 0%, #0f52ba 50%, #1a5fc9 100%);
  padding: 60px 48px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
  overflow: hidden;

  /* 医院走廊背景装饰效果 */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background:
      linear-gradient(
        to bottom,
        rgba(255, 255, 255, 0.05) 0%,
        transparent 30%
      ),
      repeating-linear-gradient(
        90deg,
        transparent,
        transparent 80px,
        rgba(255, 255, 255, 0.03) 80px,
        rgba(255, 255, 255, 0.03) 160px
      );
    pointer-events: none;
  }

  .brand-content {
    position: relative;
    z-index: 1;

    .brand-title {
      font-size: 28px;
      font-weight: 700;
      color: #ffffff;
      margin-bottom: 16px;
      line-height: 1.3;
      letter-spacing: -0.01em;
    }

    .brand-desc {
      font-size: 14px;
      line-height: 1.8;
      color: rgba(255, 255, 255, 0.8);
      max-width: 320px;
    }
  }

  .brand-footer {
    position: relative;
    z-index: 1;
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 11px;
    font-weight: 600;
    color: rgba(255, 255, 255, 0.65);
    letter-spacing: 0.06em;
  }
}

/* ========== 右侧登录表单区 ========== */
.login-form-section {
  flex: 1;
  background: #ffffff;
  padding: 60px 56px;
  display: flex;
  align-items: center;

  .form-wrapper {
    width: 100%;
    max-width: 400px;

    .form-title {
      font-size: 22px;
      font-weight: 700;
      color: #191c1e;
      margin-bottom: 8px;
      letter-spacing: -0.01em;
    }

    .form-subtitle {
      font-size: 14px;
      color: #64748b;
      margin-bottom: 36px;
      line-height: 1.5;
    }
  }
}

/* ========== 表单自定义样式 ========== */
.login-form {
  .form-item-custom {
    margin-bottom: 22px;

    .input-label {
      display: block;
      font-size: 11px;
      font-weight: 700;
      color: #64748b;
      letter-spacing: 0.06em;
      margin-bottom: 8px;
      text-transform: uppercase;
    }

    /* 输入框统一样式覆盖 */
    :deep(.el-input__wrapper) {
      border-radius: 8px;
      box-shadow: none;
      border: 1px solid #cbd5e1;
      transition: all 0.2s ease;
      padding: 4px 12px;

      &:hover {
        border-color: #94a3b8;
      }

      &.is-focus {
        border-color: #003c90;
        box-shadow: 0 0 0 3px rgba(0, 60, 144, 0.1);
      }
    }

    :deep(.el-input__inner) {
      height: 44px;
      font-size: 14px;
      color: #191c1e;

      &::placeholder {
        color: #94a3b8;
      }
    }

    :deep(.el-input__prefix) {
      color: #94a3b8;
    }
  }

  /* 验证码行特殊布局 */
  .captcha-row {
    .captcha-group {
      display: flex;
      align-items: center;
      gap: 10px;

      :deep(.el-input) {
        flex: 1;
      }

      .captcha-display {
        width: 110px;
        height: 44px;
        background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
        border: 1px solid #cbd5e1;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        user-select: none;
        transition: all 0.2s ease;

        &:hover {
          border-color: #94a3b8;
        }

        span {
          font-size: 20px;
          font-weight: 700;
          letter-spacing: 5px;
          color: #003c90;
          font-family: 'Courier New', monospace;
          text-decoration: line-through wavy rgba(0, 60, 144, 0.25);
        }
      }

      .captcha-refresh-btn {
        width: 44px !important;
        height: 44px !important;
        border: 1px solid #cbd5e1;
        background: #f8fafc;
        color: #64748b;
        flex-shrink: 0;

        &:hover {
          color: #003c90;
          border-color: #003c90;
          background: #f0f7ff;
        }
      }
    }
  }

  /* 记住我和忘记密码选项行 */
  .form-options {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24px;
    margin-top: -8px;

    .remember-me {
      :deep(.el-checkbox__label) {
        font-size: 13px;
        color: #64748b;
        font-weight: 500;
      }
    }

    .forgot-link {
      font-size: 13px;
      font-weight: 600;
      color: #003c90;
      text-decoration: none;
      transition: opacity 0.2s;

      &:hover {
        opacity: 0.75;
        text-decoration: underline;
      }
    }
  }

  /* 登录按钮 */
  .login-btn {
    width: 100%;
    height: 48px;
    font-size: 15px;
    font-weight: 600;
    border-radius: 8px;
    background: #003c90;
    border-color: #003c90;
    letter-spacing: 0.02em;
    transition: all 0.2s ease;

    &:hover:not(:disabled) {
      background: #0f52ba;
      border-color: #0f52ba;
      transform: translateY(-1px);
      box-shadow: 0 4px 16px rgba(0, 60, 144, 0.3);
    }

    &:active:not(:disabled) {
      transform: translateY(0);
    }
  }

  /* 安全提示文字 */
  .security-notice {
    text-align: center;
    font-size: 12px;
    color: #94a3b8;
    margin-top: 28px;
    line-height: 1.5;
  }
}

/* ========== 底部版权信息 ========== */
.login-footer {
  display: flex;
  justify-content: center;
  gap: 48px;
  padding: 20px 40px;
  background: #ffffff;
  border-top: 1px solid #e0e3e5;

  span {
    font-size: 11px;
    color: #94a3b8;
    white-space: nowrap;
  }
}

/* ========== 响应式适配 ========== */
@media screen and (max-width: 900px) {
  .login-brand {
    display: none;
  }

  .login-main {
    max-width: 500px;
  }

  .login-form-section {
    padding: 48px 40px;
  }
}
</style>
