<template>
  <div class="app-shell">
    <aside class="sidebar">
      <div class="brand">
        <div class="logo">小</div>
        <div>
          <h1>小哈书</h1>
          <p>发现灵感 · 记录生活 · AI 创作</p>
        </div>
      </div>

      <nav class="nav-list">
        <button v-for="item in tabs" :key="item.key" :class="['nav-item', { active: activeTab === item.key }]" @click="activeTab = item.key">
          {{ item.label }}
        </button>
      </nav>

      <div class="quick-card">
        <span class="quick-tag">Star-Friendly Demo</span>
        <h3>一键体验</h3>
        <p>登录、注册、发笔记、AI 创作助手都可以直接演示。</p>
      </div>
    </aside>

    <main class="main">
      <header class="hero">
        <div>
          <span class="hero-badge">Xiaohongshu Style</span>
          <h2>内容社区 + AI 创作助手</h2>
          <p>一个适合面试展示、学习微服务和 AI Agent 的内容平台样例。</p>
        </div>
        <div class="hero-actions">
          <button class="ghost-btn" @click="activeTab = 'agent'">试用 AI 助手</button>
          <button class="ghost-btn" @click="activeTab = 'publish'">发布笔记</button>
        </div>
      </header>

      <section v-if="activeTab === 'feed'" class="feed-grid">
        <article v-for="card in cards" :key="card.title" class="feed-card">
          <div class="feed-cover" :style="{ background: card.bg }"></div>
          <div class="feed-body">
            <div class="feed-meta">
              <span>{{ card.author }}</span>
              <span>{{ card.likes }} 喜欢</span>
            </div>
            <h3>{{ card.title }}</h3>
            <p>{{ card.desc }}</p>
          </div>
        </article>
      </section>

      <section v-else-if="activeTab === 'login'" class="panel">
        <h3>登录 / 注册</h3>
        <p>支持验证码登录和密码登录，注册将自动走后端接口。</p>
        <form class="form" @submit.prevent="handleLogin">
          <input v-model="loginForm.phone" placeholder="手机号" />
          <select v-model.number="loginForm.type">
            <option :value="1">验证码登录</option>
            <option :value="2">密码登录</option>
          </select>
          <input v-if="loginForm.type === 1" v-model="loginForm.code" placeholder="验证码" />
          <input v-else v-model="loginForm.password" type="password" placeholder="密码" />
          <button type="submit" class="primary-btn">登录</button>
        </form>
      </section>

      <section v-else-if="activeTab === 'register'" class="panel">
        <h3>注册账号</h3>
        <p>手机号注册后可直接登录，适合演示完整闭环。</p>
        <form class="form" @submit.prevent="handleRegister">
          <input v-model="registerForm.nickname" placeholder="昵称" />
          <input v-model="registerForm.phone" placeholder="手机号" />
          <input v-model="registerForm.password" type="password" placeholder="密码" />
          <button type="submit" class="primary-btn">注册</button>
        </form>
      </section>

      <section v-else-if="activeTab === 'publish'" class="panel">
        <h3>发布笔记</h3>
        <p>图文 / 视频笔记入口，支持把 AI 生成结果直接带回。</p>
        <form class="form" @submit.prevent="handlePublish">
          <input v-model="publishForm.title" placeholder="标题" />
          <textarea v-model="publishForm.content" rows="6" placeholder="写点什么吧"></textarea>
          <div class="action-row">
            <button type="button" class="ghost-btn" @click="aiGenerateTitles">AI 标题</button>
            <button type="button" class="ghost-btn" @click="aiRewrite">AI 润色</button>
            <button type="button" class="ghost-btn" @click="aiTags">AI 标签</button>
          </div>
          <div v-if="aiResult.titles.length" class="result-box">
            <h4>标题建议</h4>
            <div class="chips">
              <span v-for="title in aiResult.titles" :key="title" class="chip" @click="publishForm.title = title">{{ title }}</span>
            </div>
          </div>
          <div v-if="aiResult.rewrite" class="result-box">
            <h4>润色结果</h4>
            <p>{{ aiResult.rewrite }}</p>
          </div>
          <div v-if="aiResult.tags.length" class="result-box">
            <h4>推荐标签</h4>
            <div class="chips">
              <span v-for="tag in aiResult.tags" :key="tag" class="chip">#{{ tag }}</span>
            </div>
          </div>
          <button type="submit" class="primary-btn">发布</button>
        </form>
      </section>

      <section v-else-if="activeTab === 'agent'" class="panel">
        <div class="panel-head">
          <div>
            <h3>AI 创作助手</h3>
            <p>支持上下文、记忆和工作流式一键生成。</p>
          </div>
          <div class="stage-pill">{{ agentOutput.stage || 'WAITING' }}</div>
        </div>
        <div class="workflow-bar">
          <span v-for="step in workflowSteps" :key="step.key" :class="['workflow-step', { active: agentOutput.stage === step.key }]">{{ step.label }}</span>
        </div>
        <form class="form" @submit.prevent="runAgentAll">
          <input v-model="agentSessionId" placeholder="会话 ID，不填则自动生成" />
          <input v-model="agentPreferences" placeholder="用户偏好，例如：种草风, 干货风" />
          <textarea v-model="agentInput" rows="6" placeholder="输入一段你想润色的笔记正文"></textarea>
          <button type="submit" class="primary-btn">一键生成</button>
        </form>
        <div v-if="agentOutput.contextHints.length" class="result-box">
          <h4>上下文提示</h4>
          <div class="workflow-bar">
            <span v-for="step in workflowSteps" :key="step.key" :class="['workflow-step', { active: agentOutput.stage === step.key }]">{{ step.label }}</span>
          </div>
          <ul>
            <li v-for="hint in agentOutput.contextHints" :key="hint">{{ hint }}</li>
          </ul>
        </div>
        <div v-if="agentOutput.titles.length" class="result-box">
          <h4>生成标题</h4>
          <div class="chips">
            <span v-for="title in agentOutput.titles" :key="title" class="chip" @click="publishForm.title = title">{{ title }}</span>
          </div>
        </div>
        <div v-if="agentOutput.rewrite" class="result-box">
          <h4>润色结果</h4>
          <p>{{ agentOutput.rewrite }}</p>
        </div>
        <div v-if="agentOutput.tags.length" class="result-box">
          <h4>推荐标签</h4>
          <div class="chips">
            <span v-for="tag in agentOutput.tags" :key="tag" class="chip">#{{ tag }}</span>
          </div>
        </div>
        <div v-if="agentOutput.memorySummary" class="result-box">
          <h4>记忆摘要</h4>
          <p>{{ agentOutput.memorySummary }}</p>
        </div>
      </section>

      <section v-else class="panel profile-panel">
        <h3>个人主页</h3>
        <div class="profile-card">
          <div class="avatar">Q</div>
          <div>
            <strong>{{ profile.nickname || '未登录用户' }}</strong>
            <p>手机号：{{ profile.phone || '未填写' }}</p>
            <p>Token：{{ token || '暂无' }}</p>
          </div>
        </div>
      </section>

      <div v-if="message" class="toast">{{ message }}</div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const activeTab = ref('feed')
const message = ref('')
const token = ref(localStorage.getItem('xhs_token') || '')
const profile = ref(JSON.parse(localStorage.getItem('xhs_profile') || '{}'))

const tabs = [
  { key: 'feed', label: '首页' },
  { key: 'login', label: '登录' },
  { key: 'register', label: '注册' },
  { key: 'publish', label: '发布' },
  { key: 'agent', label: 'AI 助手' },
  { key: 'profile', label: '我的' },
]

const cards = [
  { title: '今日穿搭灵感', desc: '通勤、约会、周末出街都能用的配色思路。', author: '小哈同学', likes: '1.8w', bg: 'linear-gradient(135deg,#ff9a9e,#fad0c4)' },
  { title: '周末探店清单', desc: '咖啡、甜品、拍照点位，一次收藏到位。', author: '城市玩家', likes: '9860', bg: 'linear-gradient(135deg,#a18cd1,#fbc2eb)' },
  { title: '产品设计复盘', desc: '把一个功能拆成结构、流程、视觉三个层面。', author: '设计师阿乐', likes: '2.4w', bg: 'linear-gradient(135deg,#f6d365,#fda085)' },
]

const loginForm = ref({ phone: '', type: 1, code: '', password: '' })
const registerForm = ref({ nickname: '', phone: '', password: '' })
const publishForm = ref({ title: '', content: '' })
const aiResult = ref({ titles: [], rewrite: '', tags: [] })
const agentInput = ref('')
const agentSessionId = ref('')
const agentPreferences = ref('')
const agentOutput = ref({ stage: '', titles: [], rewrite: '', tags: [], contextHints: [], memorySummary: '' })
const workflowSteps = [
  { key: 'INTAKE_BLOCKED', label: '输入校验' },
  { key: 'GROUNDING_ONLY', label: '上下文理解' },
  { key: 'CONTEXTED_REWRITE', label: '上下文生成' },
  { key: 'MEMORY_WRITE', label: '记忆沉淀' },
]

const api = axios.create({ baseURL: '/api', timeout: 10000 })
const agentApi = axios.create({ baseURL: 'http://localhost:8090', timeout: 10000 })

const flash = (text) => {
  message.value = text
  setTimeout(() => (message.value = ''), 2200)
}

const handleLogin = async () => {
  try {
    const { data } = await api.post('/user/login', { ...loginForm.value })
    if (data?.success) {
      token.value = data.data
      localStorage.setItem('xhs_token', data.data)
      flash('登录成功')
      activeTab.value = 'profile'
      return
    }
    flash(data?.errorMessage || '登录失败')
  } catch (e) {
    flash(e?.response?.data?.errorMessage || e.message || '登录请求失败')
  }
}

const handleRegister = async () => {
  try {
    const { data } = await api.post('/user/register', registerForm.value)
    if (data?.success) {
      profile.value = { nickname: registerForm.value.nickname, phone: registerForm.value.phone, userId: data.data }
      localStorage.setItem('xhs_profile', JSON.stringify(profile.value))
      flash('注册成功')
      activeTab.value = 'login'
      return
    }
    flash(data?.errorMessage || '注册失败')
  } catch (e) {
    flash(e?.response?.data?.errorMessage || e.message || '注册请求失败')
  }
}

const handlePublish = async () => {
  try {
    const { data } = await api.post('/note/publish', publishForm.value)
    flash(data?.success ? '发布成功' : (data?.errorMessage || '发布失败'))
  } catch (e) {
    flash(e?.response?.data?.errorMessage || e.message || '发布请求失败')
  }
}

const aiGenerateTitles = async () => {
  try {
    const { data } = await agentApi.post('/agent/title', { content: publishForm.value.content || agentInput.value })
    aiResult.value.titles = data?.data || []
    flash('标题已生成')
  } catch (e) {
    flash(e?.response?.data?.errorMessage || e.message || 'AI 标题生成失败')
  }
}

const aiRewrite = async () => {
  try {
    const { data } = await agentApi.post('/agent/rewrite', { content: publishForm.value.content || agentInput.value })
    aiResult.value.rewrite = data?.data || ''
    flash('正文已润色')
  } catch (e) {
    flash(e?.response?.data?.errorMessage || e.message || 'AI 润色失败')
  }
}

const aiTags = async () => {
  try {
    const { data } = await agentApi.post('/agent/tags', { content: publishForm.value.content || agentInput.value })
    aiResult.value.tags = data?.data || []
    flash('标签已推荐')
  } catch (e) {
    flash(e?.response?.data?.errorMessage || e.message || 'AI 标签推荐失败')
  }
}

const runAgentAll = async () => {
  try {
    const { data } = await agentApi.post('/agent/run', {
      content: agentInput.value,
      sessionId: agentSessionId.value,
      preferences: agentPreferences.value,
    })
    const result = data?.data || {}
    agentSessionId.value = result.sessionId || agentSessionId.value
    agentOutput.value = {
      stage: result.stage || '',
      titles: result.titles || [],
      rewrite: result.rewrite || '',
      tags: result.tags || [],
      contextHints: result.contextHints || [],
      memorySummary: result.memorySummary || '',
    }
    flash('Agent 一键生成完成')
  } catch (e) {
    flash(e?.response?.data?.errorMessage || e.message || 'Agent 执行失败')
  }
}

const refreshProfile = () => {
  token.value = localStorage.getItem('xhs_token') || ''
  profile.value = JSON.parse(localStorage.getItem('xhs_profile') || '{}')
  flash('已刷新本地用户态')
}
</script>
