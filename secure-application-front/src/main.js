import Vue from "vue"
import "./plugins/vuetify"
import App from "./App.vue"
import Cookies from "js-cookie"
import router from "./router/"
import store from "./store"
import "./registerServiceWorker"
import "roboto-fontface/css/roboto/roboto-fontface.css"
import "font-awesome/css/font-awesome.css"
import "./../node_modules/bulma/css/bulma.css"
import i18n from "./lang/lang"
import axios from "axios"

Vue.config.productionTip = false
window.axios = require("axios")

new Vue({
  router,
  store,
  i18n,
  axios,
  render: h => h(App)
}).$mount("#app")
