<template>
  <div id="sqli">
    <div id="page1" v-show="showFirstPage">
      <article class="message is-info">
        <div class="message-header">
          <p>Czym jest SQL Injection ?</p>
        </div>
        <div class="message-body">
          SQL injection (z ang. <em>wstrzyknięcie</em>) – metoda ataku komputerowego wykorzystująca lukę w
          zabezpieczeniach aplikacji polegającą na nieodpowiednim filtrowaniu lub niedostatecznym typowaniu danych
          użytkownika, które to dane są później wykorzystywane przy wykonaniu zapytań (SQL) do bazy danych.
        </div>
      </article>

      <article class="message">
        <div class="message-header">
          <p>Przykład wrażliwego wykorzystania executeQuery</p>
        </div>
        <div class="message-body" aria-readonly="true">
          public List AccountDTO unsafeFindAccountsByCustomerId(String customerId) throws SQLException { <br />String
          sql = "select customer_id,acc_number,branch_id,balance from Accounts where customer_id = " + customerId + "'";
          <br />Connection c = dataSource.getConnection(); <br />ResultSet rs = c.createStatement().executeQuery(sql);
          <br />}
        </div>
      </article>

      <article class="message">
        <div class="message-header">
          <p>Przykład wrażliwego wykorzystania JPA</p>
        </div>
        <div class="message-body" aria-readonly="true">
          public List AccountDTO unsafeJpaFindAccountsByCustomerId(String customerId) {
          <br />
          String jql = "from Account where customerId = '" + customerId + "'"; <br />TypedQuery Account> q =
          em.createQuery(jql, Account.class); <br />return q.getResultList() .stream() .map(this::toAccountDTO)
          .collect(Collectors.toList()); <br />}
        </div>
      </article>
    </div>

    <div id="page2" v-show="showSecondPage">
      <h1>Jak temu zapobiec</h1>

      <p>
        Skoro juz wiemy czym sie objawia SQL Injection, to moze kilka sposobow na to jak sie przeciwko temu
        zabezpieczyc:
      </p>
      <br />

      <article class="message">
        <div class="message-header">
          <p>Stosowanie <em>PreparedStatements</em></p>
        </div>
        <div class="message-body" aria-readonly="true">
          public ListAccountDTO safeFindAccountsByCustomerId(String customerId) throws Exception {<br />
          String sql = "select customer_id, acc_number, branch_id, balance from Accounts where customer_id = ?";<br />
          Connection c = dataSource.getConnection();<br />
          PreparedStatement p = c.prepareStatement(sql);<br />
          p.setString(1, customerId);<br />
          ResultSet rs = p.executeQuery(sql)); <br />
          }
        </div>
      </article>

      <article class="message">
        <div class="message-header">
          <p>Stosowanie <em>TypedQuery</em> w JPA</p>
        </div>
        <div class="message-body" aria-readonly="true">
          String jql = "from Account where customerId = :customerId";<br />
          TypedQuery Account> q = em.createQuery(jql, Account.class)<br />
          .setParameter("customerId", customerId);<br />
        </div>
      </article>

      <article class="message">
        <div class="message-header">
          <p>Stosowanie <em>Criteria API</em></p>
        </div>
        <div class="message-body" aria-readonly="true">
          CriteriaBuilder cb = em.getCriteriaBuilder();<br />
          CriteriaQuery Account cq = cb.createQuery(Account.class);<br />
          Root Account root = cq.from(Account.class);<br />
          cq.select(root).where(cb.equal(root.get(Account_.customerId), customerId));<br />
          TypedQuery Account q = em.createQuery(cq);<br />
        </div>
      </article>
    </div>

    <div id="page3" v-show="showThirdPage">
      <p>
        Do tego tematu - zabezpieczenia przed SQL Injection mozna podejsc jeszcze od innej strony, czyli walidowac to co
        zostanie wprowadzone, zanim wykonamy zapytanie do bazy danych.
      </p><br/>

      <article class="message">
        <div class="message-header">
          <p>Przyklad zabezpieczenia inputu</em></p>
        </div>
        <div class="message-body" aria-readonly="true">
          private static final Set String VALID_COLUMNS_FOR_ORDER_BY = Collections.unmodifiableSet(Stream
          .of("acc_number","branch_id","balance").collect(Collectors.toCollection(HashSet::new)));
          <br />
          public List AccountDTO safeFindAccountsByCustomerId( String customerId, String orderBy) throws Exception {
          <br />
          String sql = "select " + "customer_id,acc_number,branch_id,balance from Accounts" + "where customer_id = ?
          ";<br />
          if (VALID_COLUMNS_FOR_ORDER_BY.contains(orderBy)) {<br />
          sql = sql + " order by " + orderBy;<br />
          } else {<br />
          throw new IllegalArgumentException("Nice try!");<br />
          }<br />
          Connection c = dataSource.getConnection();<br />
          PreparedStatement p = c.prepareStatement(sql);<br />
          p.setString(1,customerId);<br />
          }<br />
        </div>
      </article>

      <p>
        Bardzo często jako walidacje stosuje się np typ UUID jako identyfikator, poniżej znajduje się przykład w którym safeApi ma identyfikator jako UUID.
      </p>
      <div>
          <input
            class="input is-rounded"
            v-model="safeId"
            type="text"
            placeholder="Podaj id kursu"
            style="width: 30%"
          /><br/>
                    <input
            class="input is-rounded"
            v-model="safeName"
            type="text"
            placeholder="Podaj nazwe kursu"
            style="width: 30%"
          />
          <button class="button is-dark" @click="safeAdd" style="margin: 0 5px 0 5px">Dodaj do safeApi</button>
          <textarea v-model="safeAddPosts" rows="7" cols="80" id="result" readonly="true"></textarea>
        </div>
    </div>

    <div id="page4" v-show="showFourthPage">
      <h1>Przyklad na drop bazy danych</h1>
      <p>
        Wyszukiwanie danych. Wyszukiwarki tego typu można znaleźć na wielu stronach, część z nich jest tak samo wrażliwa
        na atak jak podany przykład. Twoim zadaniem jest usunięcie tabeli za pomoca SQL Injection. Uwaga, operacja jest
        nieodwracalna i testowe dane powrócą dopiero po ponownym włączeniu backendu. Ponizszy formularz przyjmuje nazwe
        kursu i w textarea pokazany jest wynik.
        <br />Tabela na ktorej operujemy nosi nazwe: <b>"unsafe.courses"</b> i kursy mają strukturę:
        <b>{ id: UUID, name: String }</b>
        <br />
      </p>
      <div class="row">
        <div class="column">
          <input
            class="input is-rounded"
            v-model="unsafeQuery"
            type="text"
            placeholder="Szukaj kursow po id"
            style="width: 30%"
          />
          <button class="button is-dark" @click="search" style="margin: 0 5px 0 5px">Szukaj niebezpiecznie</button>
          <button class="button is-dark" @click="hint1" style="margin: 0 5px 0 5px">Hint1</button>
          <button class="button is-dark" @click="hint2" style="margin: 0 5px 0 5px">Hint2</button>
          <br />
          <textarea v-model="unsafePosts" rows="7" cols="80" id="result" readonly="true"></textarea>
        </div>

        <div class="column">
          <input
            class="input is-rounded"
            v-model="safeQuery"
            type="text"
            placeholder="Szukaj kursow po id"
            style="width: 30%"
          />
          <button class="button is-dark" @click="searchSafely" style="margin: 0 5px 0 5px">
            Szukaj bezpiecznie
          </button>
          <br />
          <textarea v-model="safePosts" rows="7" cols="80" id="result" readonly="true"></textarea>
        </div>
      </div>

      <p>
        W tym przypadku chcąc zachować jednakową implementację safe i unsafe Api - udalo sie to zabezpieczyc poprzez ograniczenie user permission. 
        W przypadku unsafeApi uzytkownik moze doslownie wszystko na bazie danych, natomiast w przypadku safeApi uzytkownik ma mocno ograniczone pole manewru - 
        nie moze modyfikowac tabel itp.
      </p>

      <div class="modal is-active" v-show="activateHint1">
        <div class="modal-background"></div>
        <div class="modal-card">
          <header class="modal-card-head">
            <p class="modal-card-title">Hint 1</p>
            <button class="delete" aria-label="close" @click="activateHint1 = false"></button>
          </header>
          <section class="modal-card-body">
            W SQLu usuwanie tabeli wykonuje sie poprzez klauzule DROP TABLE {tableName}, kończenie jednej komendy
            następuje po znaku ';'
          </section>
          <footer class="modal-card-foot"></footer>
        </div>
      </div>

      <div class="modal is-active" v-show="activateHint2">
        <div class="modal-background"></div>
        <div class="modal-card">
          <header class="modal-card-head">
            <p class="modal-card-title">Hint 2</p>
            <button class="delete" aria-label="close" @click="activateHint2 = false"></button>
          </header>
          <section class="modal-card-body">
            Wklej jako query: 'id; DROP TABLE unsafe.courses;'
          </section>
          <footer class="modal-card-foot"></footer>
        </div>
      </div>
    </div>

    <nav class="pagination is-centered" role="navigation" aria-label="pagination">
      <ul class="pagination-list">
        <li>
          <button class="pagination-link" @click="showFirstPageAction">1</button>
        </li>
        <li>
          <button class="pagination-link" @click="showSecondPageAction">2</button>
        </li>
        <li>
          <button class="pagination-link" @click="showThirdPageAction">3</button>
        </li>
        <li>
          <button class="pagination-link" @click="showFourthPageAction">4</button>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script>
import Material from "vuetify/es5/util/colors"

export default {
  components: {},
  data: () => ({
    color: Material,
    safePosts: null,
    unsafePosts: null,

    unsafeQuery: null,
    safeQuery: null,

    active: false,
    activateHint1: false,
    activateHint2: false,

    showFirstPage: true,
    showSecondPage: false,
    showThirdPage: false,
    showFourthPage: false,
    showFifthPage: false,

    safeId: null,
    safeName: null,
    safeAddPosts: null,
        
  }),
  methods: {
    safeAdd() {
      axios
        .post(
          "http://localhost:8080/unSecureApi/courses", { id: this.safeId, name: this.safeName }
        )
        .then(response => {
          this.safeAddPosts = response.data
        })
    },
    search() {
      axios
        .get(
          "http://localhost:8080/unSecureApi/courses/runQueryGetsResultsFromDatabase?query=SELECT * FROM unsafe.courses WHERE id=" +
            this.unsafeQuery
        )
        .then(response => {
          this.unsafePosts = response.data
        })
    },
    showFirstPageAction() {
      ;(this.showFirstPage = true),
        (this.showSecondPage = false),
        (this.showThirdPage = false),
        (this.showFourthPage = false),
        (this.showFifthPage = false)
    },
    showSecondPageAction() {
      ;(this.showFirstPage = false),
        (this.showSecondPage = true),
        (this.showThirdPage = false),
        (this.showFourthPage = false),
        (this.showFifthPage = false)
    },
    showThirdPageAction() {
      ;(this.showFirstPage = false),
        (this.showSecondPage = false),
        (this.showThirdPage = true),
        (this.showFourthPage = false),
        (this.showFifthPage = false)
    },
    showFourthPageAction() {
      ;(this.showFirstPage = false),
        (this.showSecondPage = false),
        (this.showThirdPage = false),
        (this.showFourthPage = true),
        (this.showFifthPage = false)
    },
    showFifthPageAction() {
      ;(this.showFirstPage = false),
        (this.showSecondPage = false),
        (this.showThirdPage = false),
        (this.showFourthPage = false),
        (this.showFifthPage = true)
    },
    searchSafely() {
      axios
        .get(
          "http://localhost:8080/secureApi/courses/runQueryGetsResultsFromDatabase?query=SELECT * FROM safe.courses WHERE id=" +
            this.safeQuery
        )
        .then(response => {
          this.safePosts = response.data
        })
    },
    hint1() {
      this.activateHint1 = "true"
    },
    hint2() {
      this.activateHint2 = "true"
    }
  }
}
</script>

<style scoped lang="css">
.row {
  display: flex;
}

.column {
  flex: 50%;
}

textarea {
  width: 100%;
  height: 150px;
  padding: 12px 20px;
  box-sizing: border-box;
  border: 2px solid #ccc;
  border-radius: 4px;
  background-color: #f8f8f8;
  resize: none;
  margin-top: 5px;
}

h1 {
  font-size: 24px;
  font-weight: bold;
}
</style>
