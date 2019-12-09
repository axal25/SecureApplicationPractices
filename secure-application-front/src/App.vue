<template>
  <div class="app-root">
    <router-view></router-view>
    <!-- theme setting -->
    <v-btn
      small
      fab
      dark
      falt
      fixed
      top="top"
      right="right"
      class="setting-fab"
      color="blue"
      @click="openThemeSettings"
    >
      <v-icon>settings</v-icon>
    </v-btn>
    <!-- setting drawer -->
    <v-navigation-drawer class="setting-drawer" temporary right v-model="rightDrawer" hide-overlay fixed>
      <theme-settings></theme-settings>
    </v-navigation-drawer>
    <!-- global snackbar -->
    <!-- <v-snackbar :timeout="3000" bottom right :color="snackbar.color" v-model="snackbar.show">
      {{ snackbar.text }}
      <v-btn dark flat @click.native="snackbar.show = false" icon>
        <v-icon>close</v-icon>
      </v-btn>
    </v-snackbar>-->
  </div>
</template>

<script>
import ThemeSettings from "@/components/ThemeSettings"

export default {
  components: {
    ThemeSettings
  },
  data() {
    return {
      rightDrawer: false,
      snackbar: {
        show: false,
        text: "",
        color: ""
      }
    }
  },
  created() {
    // add app events
  },
  methods: {
    openThemeSettings() {
      this.$vuetify.goTo(0)
      this.rightDrawer = !this.rightDrawer
    }
  }
}
</script>

<style>
.setting-fab {
  top: 64px !important;
  right: 0;
  border-radius: 0;
}

body {
  font-size: 16px;
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: #fafafa !important;
}

.app-root {
  color: #2c3e50;
  margin: 40px 40px 40px 40px;
}
</style>
