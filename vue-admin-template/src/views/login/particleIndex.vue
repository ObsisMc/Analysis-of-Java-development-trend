<template>
  <div>
    <particles-bg type="circle" :bg="true"/>
    <div class="login">
      <transition name="slide-fade">
        <span v-if="show" @click="showTitle">
        Analysis of JAVA development's current situation
        </span>
      </transition>
      <transition name="login-fade">
        <el-button v-if="loginShow" @click="authenGithub">
          To Github
        </el-button>
      </transition>
    </div>
  </div>
</template>

<script>
import {ParticlesBg} from "particles-bg-vue";
import loginForm from "@/views/login/loginForm";
import axios from "axios";

export default {
  name: "particleIndex",
  data() {
    return {
      screenHeight: "",
      screenWidth: "",
      fontSize: "",
      show: true,
      loginShow: false

    }
  },
  methods: {
    getViewPort() {
      this.screenHeight = this.$getViewportSize().height * 0.8;
      this.screenWidth = this.$getViewportSize().width * 0.8;
      // this.fontSize = this.screenWidth /3;
      this.fontSize = 100;
    },
    showTitle() {
      if (this.show === false) {
        this.showLogin();
        setTimeout(() => {
          this.show = !this.show
        }, 500);
      } else {
        this.show = !this.show;
        setTimeout(this.showLogin, 500);
      }

    },
    showLogin() {
      this.loginShow = !this.loginShow;
    },
    authenGithub() {
      let redirect = "http://localhost:9528/#/dashboard";
      let client_id = "1b620213701eebcda787";
      let url = "https://github.com/login/oauth/authorize?client_id=1b620213701eebcda787&redirect_uri=" + redirect;
      window.location.href = url;
    }
  },
  mounted() {
    let url = window.location.href;
    let dz_url = url.split("#")[0];
    if (dz_url.indexOf("code=") !== -1) {
      let code = dz_url.split("code=")[1];
      let ident = String(Math.random() * Math.random());
      console.log(code,ident);
      axios.get("http://localhost:8080/api/auth", {
        params: {
          code: code,
          identity: ident
        }
      }).then(response =>{

      }).finally(()=>{

      })
    }

    this.getViewPort();
    window.addEventListener('resize', this.getViewPort);
  },
  components: {
    ParticlesBg,
    loginForm
  }
}
</script>

<style scoped>
.hello {
  padding: 32px;
  background-color: rgb(240, 242, 245, 0.3);
  position: relative;
}

.login {
  display: block;
  position: absolute;
  transform: translate(-50%, -61.8%);
  /*左右方向的位置为50%, 上下方向的位置为黄金分割比例0.618*/

  left: 55%;
  top: 50%;
  width: 80%;
  /*padding:50px;*/

  border-radius: 8px;

  /*登录框4个角设置为圆角*/
  /*background: rgba(240, 242, 245, 0.5);*/
  /*白色背景,不透明度90%*/
}

@font-face {
  font-family: myFirstFont;
  src: url("Futura/Futura-Heavy-4.ttf");
}

span {
  font-family: myFirstFont;
  color: black;
  font-size: 60px;
  font-weight: 450;
}

.slide-fade-enter-active {
  transition: all .3s ease;
}

.slide-fade-leave-active {
  transition: all .3s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
  transform: translateX(10px);
  opacity: 0;
}

.login-fade-enter-active {
  transition: all .3s ease;
}

.login-fade-leave-active {
  transition: all .3s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.login-fade-enter, .login-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
  transform: translateX(10px);
  opacity: 0;
}

</style>
