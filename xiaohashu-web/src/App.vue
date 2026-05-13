<template>
  <div class="app-shell">
    <aside class="sidebar">
      <div class="brand">
        <div class="logo">小</div>
        <div>
          <h1>小哈书</h1>
          <p>仿小红书风格的测试前端</p>
        </div>
      </div>

      <nav class="nav">
        <button :class="{ active: tab === 'feed' }" @click="tab = 'feed'">首页</button>
        <button :class="{ active: tab === 'login' }" @click="tab = 'login'">登录</button>
        <button :class="{ active: tab === 'register' }" @click="tab = 'register'">注册</button>
        <button :class="{ active: tab === 'publish' }" @click="tab = 'publish'">发笔记</button>
      </nav>

      <div class="tips">
        <h3>测试说明</h3>
        <ul>
          <li>默认对接本地后端 `/api`。</li>
          <li>登录/注册表单支持你后续接真实接口。</li>
          <li>首页用于先看整体视觉效果。</li>
        </ul>
      </div>
    </aside>

    <main class="content">
      <section v-if="tab === 'feed'" class="feed-view">
        <header class="hero">
          <div>
            <span class="badge">Discover</span>
            <h2>发现灵感，记录生活</h2>
            <p>先做一个接近小红书的沉浸式页面，便于后续联调登录、注册、发布笔记。</p>
          </div>
          <div class="hero-card">
            <div class="stat">
              <strong>128k</strong>
              <span>笔记</span>
            </div>
            <div class="stat">
              <strong>56k</strong>
              <span>创作者</span>
            </div>
          </div>
        </header>

        <div class="grid">
          <article v-for="card in cards" :key="card.title" class="note-card">
            <div class="cover" :style="{ background: card.bg }"></div>
            <div class="note-body">
              <h3>{{ card.title }}</h3>
              <p>{{ card.desc }}</p>
              <div class="meta">
                <span>{{ card.author }}</span>
                <span>{{ card.likes }} 喜欢</span>
              </div>
            </div>
          </article>
        </div>
      </section>

      <section v-else-if="tab === 'login'" class="form-card">
        <h2>欢迎回来</h2>
        <p>使用手机号 + 密码登录，后续可直接接你的后端接口。</p>
        <form class="form" @submit.prevent="submitLogin">
          <input v-model="login.phone" placeholder="手机号" />
          <input v-model="login.password" type="password" placeholder="密码" />
          <button type="submit">登录</button>
        </form>
      </section>

      <section v-else-if="tab === 'register'" class="form-card">
        <h2>创建账号</h2>
        <p>先做基础注册页，后续你可以补短信验证码联动。</p>
        <form class="form" @submit.prevent="submitRegister">
          <input v-model="register.nickname" placeholder="昵称" />
          <input v-model="register.phone" placeholder="手机号" />
          <input v-model="register.password" type="password" placeholder="密码" />
          <button type="submit">注册</button>
        </form>
      </section>

      <section v-else class="form-card">
        <h2>发布笔记测试</h2>
        <p>这里先做一个前端壳，方便后面接笔记发布接口。</p>
        <form class="form" @submit.prevent="submitPublish">
          <input v-model="publish.title" placeholder="标题" />
          <textarea v-model="publish.content" rows="5" placeholder="正文内容"></textarea>
          <button type="submit">发布</button>
        </form>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const tab = ref('feed')
const login = ref({ phone: '', password: '' })
const register = ref({ nickname: '', phone: '', password: '' })
const publish = ref({ title: '', content: '' })

const cards = [
  { title: '春日穿搭灵感', desc: '轻松配色、日常出街、通勤也能很好看。', author: '小哈同学', likes: '1.8w', bg: 'linear-gradient(135deg,#ff9a9e,#fad0c4)' },
  { title: '周末探店笔记', desc: '咖啡、甜品、拍照机位，一次打卡收藏。', author: '城市玩家', likes: '9860', bg: 'linear-gradient(135deg,#a18cd1,#fbc2eb)' },
  { title: '产品设计复盘', desc: '把灵感拆成结构、流程、视觉三部分。', author: '设计师阿乐', likes: '2.4w', bg: 'linear-gradient(135deg,#f6d365,#fda085)' },
]

const submitLogin = () => alert(`登录测试：${login.value.phone}`)
const submitRegister = () => alert(`注册测试：${register.value.phone}`)
const submitPublish = () => alert(`发布测试：${publish.value.title}`)
</script>
