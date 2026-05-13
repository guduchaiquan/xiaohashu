<template>
  <div class="shell">
    <aside class="mobile-nav">
      <div class="brand">
        <div class="logo">小</div>
        <div>
          <h1>小哈书</h1>
          <p>仿小红书移动端体验</p>
        </div>
      </div>
      <div class="nav-list">
        <button v-for="item in tabs" :key="item.key" :class="['nav-item', { active: activeTab === item.key }]" @click="activeTab = item.key">
          {{ item.label }}
        </button>
      </div>
    </aside>

    <main class="main">
      <header class="topbar">
        <div>
          <span class="tag">Xiaohongshu Style</span>
          <h2>发现灵感、记录生活、发布笔记</h2>
        </div>
        <button class="ghost-btn" @click="refreshProfile">刷新用户态</button>
      </header>

      <section v-if="activeTab === 'feed'" class="feed">
        <article v-for="card in cards" :key="card.title" class="card note">
          <div class="cover" :style="{ background: card.bg }"></div>
          <div class="card-body">
            <div class="card-meta">
              <span>{{ card.author }}</span>
              <span>{{ card.likes }} 喜欢</span>
            </div>
            <h3>{{ card.title }}</h3>
            <p>{{ card.desc }}</p>
          </div>
        </article>
      </section>

      <section v-else-if="activeTab === 'login'" class="card auth-panel">
        <div class="panel-head">
          <h3>登录 / 注册</h3>
          <p>支持验证码登录和密码登录，注册将自动走后端接口。</p>
        </div>
        <form class="form" @submit.prevent="handleLogin">
          <label>
            手机号
            <input v-model="loginForm.phone" placeholder="请输入手机号" />
          </label>
          <label>
            登录类型
            <select v-model.number="loginForm.type">
              <option :value="1">验证码登录</option>
              <option :value="2">密码登录</option>
            </select>
          </label>
          <label v-if="loginForm.type === 1">
            验证码
            <input v-model="loginForm.code" placeholder="短信验证码" />
          </label>
          <label v-else>
            密码
            <input v-model="loginForm.password" type="password" placeholder="密码" />
          </label>
          <button type="submit">登录</button>
        </form>
      </section>

      <section v-else-if="activeTab === 'register'" class="card auth-panel">
        <div class="panel-head">
          <h3>注册账号</h3>
          <p>先完成手机号注册，后续你可以再补短信验证码。</p>
        </div>
        <form class="form" @submit.prevent="handleRegister">
          <label>
            昵称
            <input v-model="registerForm.nickname" placeholder="请输入昵称" />
          </label>
          <label>
            手机号
            <input v-model="registerForm.phone" placeholder="请输入手机号" />
          </label>
          <label>
            密码
            <input v-model="registerForm.password" type="password" placeholder="请输入密码" />
          </label>
          <button type="submit">注册</button>
        </form>
      </section>

      <section v-else-if="activeTab === 'publish'" class="card auth-panel">
        <div class="panel-head">
          <h3>发布笔记</h3>
          <p>可测试图文笔记的发布流程。</p>
        </div>
        <form class="form" @submit.prevent="handlePublish">
          <label>
            标题
            <input v-model="publishForm.title" placeholder="标题" />
          </label>
          <label>
            正文
            <textarea v-model="publishForm.content" rows="6" placeholder="写点什么吧"></textarea>
          </label>
          <div class="action-row">
            <button type="button" class="ghost-btn" @click="aiGenerateTitles">AI 标题</button>
            <button type="button" class="ghost-btn" @click="aiRewrite">AI 润色</button>
            <button type="button" class="ghost-btn" @click="aiTags">AI 标签</button>
          </div>
          <div v-if="aiResult.titles.length" class="ai-box">
            <h4>标题建议</h4>
            <div class="chips">
              <span v-for="title in aiResult.titles" :key="title" class="chip" @click="publishForm.title = title">{{ title }}</span>
            </div>
          </div>
          <div v-if="aiResult.rewrite" class="ai-box">
            <h4>润色结果</h4>
            <p>{{ aiResult.rewrite }}</p>
          </div>
          <div v-if="aiResult.tags.length" class="ai-box">
            <h4>推荐标签</h4>
            <div class="chips">
              <span v-for="tag in aiResult.tags" :key="tag" class="chip">#{{ tag }}</span>
            </div>
          </div>
          <button type="submit">发布</button>
        </form>
      </section>

      <section v-else-if="activeTab === 'agent'" class="card auth-panel">
        <div class="panel-head">
          <h3>AI 创作助手</h3>
          <p>这个页面用于测试 LangGraph / Agent 能力。</p>
        </div>
        <form class="form" @submit.prevent="runAgentAll">
          <label>
            输入内容
            <textarea v-model="agentInput" rows="6" placeholder="输入一段你想润色的笔记正文"></textarea>
          </label>
          <button type="submit">一键生成</button>
        </form>
        <div v-if="agentOutput.titles.length" class="ai-box">
          <h4>生成标题</h4>
          <div class="chips">
            <span v-for="title in agentOutput.titles" :key="title" class="chip" @click="publishForm.title = title">{{ title }}</span>
          </div>
        </div>
        <div v-if="agentOutput.rewrite" class="ai-box">
          <h4>润色结果</h4>
          <p>{{ agentOutput.rewrite }}</p>
        </div>
        <div v-if="agentOutput.tags.length" class="ai-box">
          <h4>推荐标签</h4>
          <div class="chips">
            <span v-for="tag in agentOutput.tags" :key="tag" class="chip">#{{ tag }}</span>
          </div>
        </div>
      </section>

      <section v-else class="card auth-panel">
        <div class="panel-head">
          <h3>个人主页</h3>
          <p>展示用户状态、登录信息和测试结果。</p>
        </div>
        <div class="profile-box">
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
const agentOutput = ref({ titles: [], rewrite: '', tags: [] })

const api = axios.create({ baseURL: '/api', timeout: 10000 })
const agentApi = axios.create({ baseURL: 'http://localhost:8090', timeout: 10000 })

const flash = (text) => {
  message.value = text
  setTimeout(() => (message.value = ''), 2200)
}

const handleLogin = async () => {
  try {
    const payload = { ...loginForm.value }
    const { data } = await api.post('/user/login', payload)
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
    const content = agentInput.value
    const [titles, rewrite, tags] = await Promise.all([
      agentApi.post('/agent/title', { content }),
      agentApi.post('/agent/rewrite', { content }),
      agentApi.post('/agent/tags', { content }),
    ])
    agentOutput.value = {
      titles: titles.data?.data || [],
      rewrite: rewrite.data?.data || '',
      tags: tags.data?.data || [],
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