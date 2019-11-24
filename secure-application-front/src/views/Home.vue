<template>
  <div id="home">
    <h2>
      <b>Studio Projektowe 2: Platforma Szkoleniowa</b>
    </h2>
    <p>
      Projekt zakłada stworzenie platformy szkoleniowej na temat bezpieczeństwa aplikacji internetowych. Projekt ma
      formę strony wrażliwej na ataki.
    </p>

    <br />
    <br />
    <br />
    <br />

    <div id="authors">
      <p>Projekt realizowany jest przez:</p>
      <b>Łukasz Stawowy</b> <br /><b>Jacek Oleś</b>
    </div>
  </div>
</template>

<script>
import Material from "vuetify/es5/util/colors"

export default {
  components: {},
  data: () => ({
    color: Material
  })
}
</script>

<style scoped lang="css">
#home {
  color: #2e5894;
  text-shadow: rgba(61, 61, 61, 0.3) 1px 1px, rgba(61, 61, 61, 0.2) 2px 2px;
}
</style>
