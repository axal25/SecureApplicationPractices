<template>
  <div id="sqli">
    <h2>Czym jest SQL Injection ?</h2>
    <p>
      <br />Na początku przytoczę definicję: <br />SQL injection (z ang. <em>wstrzyknięcie</em>) – metoda ataku
      komputerowego wykorzystująca lukę w zabezpieczeniach aplikacji polegającą na nieodpowiednim filtrowaniu lub
      niedostatecznym typowaniu danych użytkownika, które to dane są później wykorzystywane przy wykonaniu zapytań (SQL)
      do bazy danych.
    </p>

    <h2>Jak się przejawia w kodzie</h2>

    <p>Wszystkie przykłady są z Javy.</p>

    <textarea rows="7" cols="100" readonly="true" style="border:solid 2px black; margin: 5px;">
public List<AccountDTO> unsafeFindAccountsByCustomerId(String customerId) throws SQLException {
  String sql = "select customer_id,acc_number,branch_id,balance from Accounts where customer_id = " + customerId + "'";
  Connection c = dataSource.getConnection();
  ResultSet rs = c.createStatement().executeQuery(sql);
}
      </textarea
    >

    <p>Nawet JPA nie zapewnia zawsze bezpieczeństwa - poniżej przykład wrażliwego dostępu za pomocą JPA.</p>

    <textarea rows="7" cols="100" readonly="true" style="border:solid 2px black; margin: 5px;">
public List<AccountDTO> unsafeJpaFindAccountsByCustomerId(String customerId) {    
  String jql = "from Account where customerId = '" + customerId + "'";        
  TypedQuery Account> q = em.createQuery(jql, Account.class);        
  return q.getResultList()
    .stream()
    .map(this::toAccountDTO)
    .collect(Collectors.toList());        
}
      </textarea
    >

    <h2>Przyklad</h2>
    <p>
      Wyszukiwanie danych. Wyszukiwarki tego typu można znaleźć na wielu stronach, część z nich jest tak samo wrażliwa
      na atak jak podany przykład. Twoim zadaniem jest zdobyć informacje na temat

      <br />
      <input v-model="query" type="text" placeholder="Szukaj uzytkownikow" class="form-control" />
      <mdb-btn large color="primary" @click.native="search" style="border:solid 2px black; margin: 5px;"
        >Szukaj</mdb-btn
      >
      <mdb-btn large color="primary" @click.native="hint1" style="border:solid 2px black; margin: 5px;">Hint1</mdb-btn>
      <mdb-btn large color="primary" @click.native="hint2" style="border:solid 2px black; margin: 5px;">Hint2</mdb-btn>
      <textarea v-model="posts" rows="7" cols="100" id="result" style="border:solid 2px black; margin: 5px;"></textarea>
    </p>

    <h2>Jak temu zapobiec</h2>

    <p>
      Pierwszym przykładem jak zabezpieczyć dostęp do bazy to: stosowanie
      <em>prepared statements</em>
    </p>

    <textarea rows="7" cols="100" readonly="true" style="border:solid 2px black; margin: 5px;">
public ListAccountDTO> safeFindAccountsByCustomerId(String customerId) throws Exception {
  String sql = "select customer_id, acc_number, branch_id, balance from Accounts where customer_id = ?";
  Connection c = dataSource.getConnection();
  PreparedStatement p = c.prepareStatement(sql);
  p.setString(1, customerId);
  ResultSet rs = p.executeQuery(sql)); 
}
    </textarea>

    <p>
      Kolejno w przypadku JPA stosujemy cos w tym stylu
    </p>

    <textarea rows="3" cols="100" readonly="true" style="border:solid 2px black; margin: 5px;">
String jql = "from Account where customerId = :customerId";
TypedQuery Account> q = em.createQuery(jql, Account.class)
.setParameter("customerId", customerId);
    </textarea>

    <p>
      Nastepne zastosowanie to uzycie Criteria API - jedno z zagadnien poznanych na SOA.
    </p>

    <textarea rows="5" cols="100" readonly="true" style="border:solid 2px black; margin: 5px;">
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<Account> cq = cb.createQuery(Account.class);
Root<Account> root = cq.from(Account.class);
cq.select(root).where(cb.equal(root.get(Account_.customerId), customerId));
TypedQuery<Account> q = em.createQuery(cq);
    </textarea>

    <p>
      Do tego tematu - zabezpieczenia przed SQL Injection mozna podejsc jeszcze od innej strony, czyli walidowac to co
      zostanie wprowadzone, zanim wykonamy zapytanie do bazy danych.
    </p>

    <textarea rows="7" cols="100" readonly="true" style="border:solid 2px black; margin: 5px;">
private static final Set<String> VALID_COLUMNS_FOR_ORDER_BY
  = Collections.unmodifiableSet(Stream
      .of("acc_number","branch_id","balance")
      .collect(Collectors.toCollection(HashSet::new)));
 
public List<AccountDTO> safeFindAccountsByCustomerId(
  String customerId,
  String orderBy) throws Exception { 
    String sql = "select "
      + "customer_id,acc_number,branch_id,balance from Accounts"
      + "where customer_id = ? ";
    if (VALID_COLUMNS_FOR_ORDER_BY.contains(orderBy)) {
        sql = sql + " order by " + orderBy;
    } else {
        throw new IllegalArgumentException("Nice try!");
    }
    Connection c = dataSource.getConnection();
    PreparedStatement p = c.prepareStatement(sql);
    p.setString(1,customerId);
    // ... result set processing omitted
}
    </textarea>
  </div>
</template>

<script>
import Material from "vuetify/es5/util/colors"
import { mdbBtn } from "mdbvue"

export default {
  components: { mdbBtn },
  data: () => ({
    color: Material,
    posts: null,
    query: null,
    active: false
  }),
  methods: {
    search() {
      axios.post("https://localhost:8080/courses/String/" + value).then(response => {
        this.posts = response.data
      })
    },
    hint1() {
      alert("Hint 1 pressed")
    },
    hint2() {
      alert("Hint 2 pressed")
    }
  }
}
</script>

<style scoped lang="css">
#sqli {
  color: blue;
  text-shadow: rgba(61, 61, 61, 0.3) 1px 1px, rgba(61, 61, 61, 0.2) 2px 2px;
}

#h2 {
  color: blueviolet;
  font-weight: bolder;
}

#h3 {
  color: blueviolet;
  font-weight: bolder;
}
</style>