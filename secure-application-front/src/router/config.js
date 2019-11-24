import { AuthLayout, DefaultLayout } from "@/components/layouts"

export const publicRoute = [
  { path: "*", component: () => import(/* webpackChunkName: "errors-404" */ "@/views/error/NotFound.vue") },
  {
    path: "/auth",
    component: AuthLayout,
    meta: { title: "Login" },
    redirect: "/auth/login",
    hidden: true,
    children: [
      {
        path: "login",
        name: "login",
        meta: { title: "Login" },
        component: () => import(/* webpackChunkName: "login" */ "@/views/auth/Login.vue")
      }
    ]
  },

  {
    path: "/404",
    name: "404",
    meta: { title: "Not Found" },
    component: () => import(/* webpackChunkName: "errors-404" */ "@/views/error/NotFound.vue")
  },

  {
    path: "/500",
    name: "500",
    meta: { title: "Server Error" },
    component: () => import(/* webpackChunkName: "errors-500" */ "@/views/error/Error.vue")
  }
]

export const protectedRoute = [
  {
    path: "/",
    component: DefaultLayout,
    meta: { title: "Home", group: "home", icon: "" },
    redirect: "/home",
    children: [
      {
        path: "/home",
        name: "Home",
        meta: { title: "Home", group: "home", icon: "dashboard" },
        component: () => import(/* webpackChunkName: "dashboard" */ "@/views/Home.vue")
      },

      {
        path: "/sqli",
        meta: { title: "SQL Injection", group: "attacks", icon: "" },
        name: "SQLi",
        // props: route => ({ type: route.query.type }),
        component: () => import(/* webpackChunkName: "routes" */ `@/views/SQLi.vue`)
      },

      {
        path: "/http-xss",
        meta: { title: "Http XSS", group: "attacks", icon: "media" },
        name: "HttpXSS",
        props: route => ({ type: route.query.type }),
        component: () => import(/* webpackChunkName: "routes" */ `@/views/HttpXSS.vue`)
      },

      {
        path: "/js-xss",
        meta: { title: "Js XSS", group: "attacks", icon: "media" },
        name: "JsXSS",
        props: route => ({ type: route.query.type }),
        component: () => import(/* webpackChunkName: "routes" */ `@/views/JsXSS.vue`)
      },

      {
        path: "/aboutus",
        meta: { title: "AboutUs", group: "other", icon: "media" },
        name: "AboutUs",
        props: route => ({ type: route.query.type }),
        component: () => import(/* webpackChunkName: "routes" */ `@/views/AboutUs.vue`)
      },

      {
        path: "/aboutproject",
        meta: { title: "AboutProject", group: "other", icon: "media" },
        name: "AboutProject",
        props: route => ({ type: route.query.type }),
        component: () => import(/* webpackChunkName: "routes" */ `@/views/AboutProject.vue`)
      },

      {
        path: "/403",
        name: "Forbidden",
        meta: { title: "Access Denied", hiddenInMenu: true },
        component: () => import(/* webpackChunkName: "error-403" */ "@/views/error/Deny.vue")
      }
    ]
  }
]
