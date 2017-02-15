## Dokumentasi Project ##

# Setup Development Environment
Untuk memulai Development sistem ini user diharuskan sudah memenuhi Development Environment sebagai berikut :
- JDK 1.8
- MySQL (Database)
- Tomcat (Web Server)

# Membuat Resource Server Sederhana
Pertama kita akan membuat resource server sedehana menggunakan Spring Framework.
Step by Step nya adalah sebagai berikut :
- Download Setup projek di `http://start.spring.io`
- Isi projek metadata berupa :
  * artifact id :
  * lupa
- Isi dependency yang dubutuhkan berupa :
  * web
  * thymeleaf
- Download lalu pindahkan ke text editor
- Kita coba bikin Controller sebagi resources (resource-server/src/main/java/domain/controller/HaloController)
  ```
  @Controller
  public class ResourceServerController{

  }
  ```
- Kita bikin methode untuk di proses ke user. Methode ini akan mengembalikan waktu sekarang menggunakan variabel `waktu`. methode ini dapat diakses di url /halo.(resource-server/src/main/java/domain/controller/HaloController)
  ```
    @RequestMapping("/halo")
    public void halo(Model m, @RequestParam(required = false) String nama){
        m.addAttribute("waktu", new Date());
        if(nama != null && !nama.isEmpty()){
            m.addAttribute("pesan", "Halo "+nama);
        }
    }
    @CrossOrigin
    @RequestMapping("/api/halo")
    @ResponseBody
    public Map<String, Object> haloApi(@RequestBody(required = false) Map<String, String> input){
        Map<String, Object> data = new HashMap<>();
        data.put("waktu", new Date());

        if(input != null){
            String nama = input.get("nama");
            if(nama != null && !nama.isEmpty()){
                data.put("pesan", "Halo "+nama);
            }
        }

        return data;
    }
  ```
- Kita bikin UI untuk mengeluarkan informasi dari methode halo yang telah buat tadi.(resource-server/src/main/resources/templates/halo.html)
  ```
    <html>
        <head>
            <title>Halo Spring Boot</title>
        </head>
        <body>
            <h1>Halo Spring Boot</h1>
            <h2>Waktu saat ini : <span th:text="${waktu}"></span></h2>
            <h3>Pesan : <span th:text="*{pesan}?:''">Halo</span></h3>
        </body>
    </html>
  ```
# Proteksi Resource Server Dengan Spring Security
- Lalu coba kita proteksi Resource Server kita menggunakan Spring Security dengan menambahkan dependency di pom.xml. (resource-server/pom.xml)
  ```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  ```
  Pada keadaan ini kita akan diminta otorisasi jika kita mengakses di browser dengan username : `user` dan password yang ada pada CLI. karena tidak efisien kita akan tambahkan konfigurasi untuk otorisasi.
- Buat class untuk konfigurasi otorisasi (/resource-server/src/main/java/domain/config/KonfigurasiSecurity)
  ```
  @Configuration
  public class KonfigurasiSecurity  extends WebSecurityConfigurerAdapter {

  }
  ```
- Buat methode untuk mengkonfigurasi otorisasi(/resource-server/src/main/java/domain/config/KonfigurasiSecurity.java)
  ```
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      auth.inMemoryAuthentication()
              .withUser("admin")
                  .password("123")
                  .roles("USER");
  }
  ```
  Pada keadaan ini kita sudah dapat login tapi masih dengan interface dari browser. Selanjutnya kita akan konfig untuk interface htmlnya.
- Konfigurasi UI untuk login(/resource-server/src/main/java/domain/config/KonfigurasiSecurity.java)
  ```
  @Override
  protected void configure(HttpSecurity http) throws Exception {
      http
              .authorizeRequests()
              .anyRequest().authenticated()
              .and()
              .formLogin().loginPage("/login").permitAll()
              .defaultSuccessUrl("/halo")
              .and()
              .logout();
  }
  ```
  Pada keadaan ini kita sudah dapat login dengan UI html yang disediakan oleh spring
- Membuat UI html sendiri(/resource-server/src/main/resources/templates/login.html)
  ```
  <html>
      <head>
          <title>Login Page</title>
          <!-- Latest compiled and minified CSS -->
          <link rel="stylesheet"
                href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"/>

          <!-- Optional theme -->
          <link rel="stylesheet"
                href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"/>

          <link rel="stylesheet"
                href="css/aplikasi.css"/>

      </head>
      <body>
          <div class="container">

            <form class="form-signin" th:action="@{/login}" method="post">

                <div th:if="${param.error}" class="alert alert-error">    
                      Invalid username and password.
                  </div>
                  <div th:if="${param.logout}" class="alert alert-success">
                      You have been logged out.
                  </div>

              <h2 class="form-signin-heading">Please sign in</h2>
              <label for="username" class="sr-only">Email address</label>
              <input type="text" id="username" name="username" class="form-control"
                     placeholder="Username"/>
              <label for="password" class="sr-only">Password</label>
              <input type="password" id="password" name="password"
                     class="form-control" placeholder="Password" />
              <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
            </form>

          </div> <!-- /container -->        

          <!-- Latest compiled and minified JavaScript -->
          <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"/>
      </body>
  </html>

  ```
  Selanjutnya kita akan mendaftarkan template ini agar dapat diproses sebagai template yang tidak memiliki controller
- Membuat Class KonfigurasiWeb(/resource-server/src/main/java/domain/config/Web.java)
  ```
  @Configuration
  public class KonfigurasiWeb extends WebMvcConfigurerAdapter {

  }
  ```
- Buat methode untuk mendaftarkan template(/resource-server/src/main/java/domain/config/Web.java)
  ```
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
      registry.addViewController("/login").setViewName("login");
  }
  ```
- Selanjutnya kita akan tambahkan fasilitas logout pada UI(/resource-server/src/main/resources/templates/halo.html)
  ```
  <form name="f" th:action="@{/logout}" method="post">
      <input type="submit" value="Logout" />
  </form>
  ```
# Integrasi otorisasi Resources Server ke database
Selanjutnya kita akan pindahkan otorisasi kita yang ada di (/resource-server/src/main/java/domain/config/KonfigurasiSecurity.java) ke database.
- Pertama kita tambahkan dependency  (/resource-server/pom.xml)
  ```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>mysql-connector</artifactId>
    </dependency>
  ```
- Lalu kita akan konfigurasi database di (/resource-server/src/main/resources/application.properties)
  ```
  spring.datasource.url=jdbc:mysql://localhost:3306/belajar_oauth
  spring.datasource.username=belajar
  spring.datasource.password=belajar
  ```
- Generate JPA(/resource-server/src/main/resources/application.properties)
  ```
  spring.jpa.generate-ddl=true
  ```
- Pertama kita akan buat database dulu(CLI)
  ```
      mysql -u root -p
      grant all on belajar_oauth.* to belajar@localhost identified by 'belajar'
      create database belajar_oauth;
    ```
- Cara menggunakan mysql (CLI)
    ```
      use pelatihan;			///menggunakan tabel
      show tables;			///memperlihatkan tabel dan datanya
      show create table peserta \G	///memperlihatkan detil tabel
      drop table peserta;		///redeploy tabel
    ```
- buat super user (CLI)
  ```
  drop table if exists s_users;

  create table s_users (
      id INT PRIMARY KEY AUTO_INCREMENT,
      username VARCHAR(100) UNIQUE NOT NULL,
      password VARCHAR(255) NOT NULL,
      enabled BOOLEAN
  ) Engine=InnoDB;

  insert into s_users (username, password, enabled)
  values ('admin', '123', true);
  insert into s_users (username, password, enabled)
  values ('ciazhar', '123', true);

  create table s_permissions (
      id INT PRIMARY KEY AUTO_INCREMENT,
      id_user VARCHAR(100) NOT NULL REFERENCES s_users(id),
      user_role VARCHAR(255) NOT NULL
  ) Engine=InnoDB;

  insert into s_permissions (id_user, user_role)
  values (1, 'ROLE_SUPERVISOR');

  insert into s_permissions (id_user, user_role)
  values (1, 'ROLE_OPERATOR');

  insert into s_permissions (id_user, user_role)
  values (2, 'ROLE_OPERATOR');
  ```
- pindah otorisasi ke database (/resource-server/src/main/java/domain/config/KonfigurasiSecurity)
  ```
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, "
                        + "enabled from s_users where username=?")
                .authoritiesByUsernameQuery("select u.username, p.user_role  "
                        + "from s_users u  "
                        + "inner join s_permissions p on u.id = p.id_user "
                        + "where u.username=?");
    }
  ```
# Memisahkan otorisai pada resources server agar menjadi authorization server tersendiri
- Selanjutnya kita akan kita pisahkan Aplikasi yang telah kita buat kemarin menjadi resources server dan Authorization server. Pertama kita akan buat dulu aplikasi untuk authorization server, seperti Pertama kali kita membuat project resources server. Step by Step nya adalah sebagai berikut :
  - Download Setup projek di `http://start.spring.io`
  - Isi projek metadata berupa :
    * artifact id :
    * lupa
  - Isi dependency yang dubutuhkan berupa :
    * web
    * thymeleaf
    * security
    * jdbc
    * mysql
  - Download lalu pindahkan ke text editor
- Lalu kita akan pindahkan file file otorisasi sebagai berikut :
  - KonfigurasiSecurity.java
  - KonfigurasiWeb.java
  - login.html
  kedalam projek authorization server kita

# Konfigurasi Resource Server dan Authorization Sever agar dapat berhubungan
Ada beberapa file yang perlu dikonfigurasi yaitu :
- pom.xml kedua aplikasi
- KonfigurasiResourceServer.java
- KonfigurasiAuthorizationServer.java
- application.properties kedua aplikasi
- KonfigurasiSecurity

## Konfigurasi Resource Server ##
- Tambahkan dependeny OAuth2(resource-server/pom.xml)
  ```
  <dependency>
      <groupId>org.springframework.security.oauth</groupId>
      <artifactId>spring-security-oauth2</artifactId>
      <version>2.0.7.RELEASE</version>
  </dependency>
  ```
- setup security (resource-server/src/main/resources/application.properties)
  ```
  logging.level.org.springframework.security=DEBUG
  ```
- Buat class KonfigurasiResourceServer (resource-server/src/main/java/domain/config/KonfigurasiResourceServer.java)
  ```
  @Configuration
  public class KonfigurasiResourceServer {

  ```
- Setup RESOURCE_ID(resource-server/src/main/java/domain/config/KonfigurasiResourceServer.java)
  ```
    private static final String RESOURCE_ID = "belajar";
  ```
- Buat inner class ResourceServerConfiguration (resource-server/src/main/java/domain/config/KonfigurasiResourceServer.java)
  ```
  @Configuration
  @EnableResourceServer
  protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  }
  ```
- Buat methode (resource-server/src/main/java/domain/config/KonfigurasiResourceServer.java/ResourceServerConfiguration). Methode ini berfungsi untuk menyuruh authorization server melakukan pengecekan apakah access token valid atau tidak.
  ```
  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
      RemoteTokenServices tokenService = new RemoteTokenServices();
      tokenService.setClientId("clientauthcode");
      tokenService.setClientSecret("123456");
      tokenService.setCheckTokenEndpointUrl("http://localhost:10000/oauth/check_token");
      resources
              .resourceId(RESOURCE_ID)
              .tokenServices(tokenService);
  }
  ```
- Buat methode(resource-server/src/main/java/domain/config/KonfigurasiResourceServer.java/ResourceServerConfiguration). Methode ini berfungsi untuk memgatur api apa saja  yang dapat diakses dan oleh siapa saja yang dapat mengakses.
  ```
  @Override
  public void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
              .antMatchers("/api/halo").hasRole("OPERATOR")
              .antMatchers("/api/waktu").authenticated();
  }
  ```
## Konfigurasi Authorization Server ##
- Tambahkan dependeny OAuth2(auth-server/pom.xml)
  ```
  <dependency>
      <groupId>org.springframework.security.oauth</groupId>
      <artifactId>spring-security-oauth2</artifactId>
      <version>2.0.7.RELEASE</version>
  </dependency>
  ```
- setup security (auth-server/src/main/resources/application.properties)
  ```
  logging.level.org.springframework.security=DEBUG
  server.port=10000
  ```
- Buat class KonfigurasiAuthorizationServer (auth-server/src/main/java/domain/config/KonfigurasiAuthorizationServer.java)
  ```
  @Configuration
  public class KonfigurasiAuthorizationServer {
  
  }
  ```
- Buat inner class AuthorizationServerConfiguration (auth-server/src/main/java/domain/config/KonfigurasiAuthorizationServer.java)
  ```
  @Configuration
  @EnableAuthorizationServer
  protected static class AuthorizationServerConfiguration extends
           AuthorizationServerConfigurerAdapter {
  
  }
  ```
- Setup RESOURCE_ID (auth-server/src/main/java/domain/config/KonfigurasiAuthorizationServer.java)
  ```
  private static final String RESOURCE_ID = "belajar";
  ```
- Setup AuthenticationManager
  ```
  @Autowired
  @Qualifier("authenticationManagerBean")
  private AuthenticationManager authenticationManager;
  ```
- Buat methode (auth-server/src/main/java/domain/config/KonfigurasiAuthorizationServer.java/AuthorizationServerConfiguration). Methode ini berfungsi untuk menyimpan token yang nanti akan di cek kembali
  ```
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints)
          throws Exception {
      endpoints
              .tokenStore(new InMemoryTokenStore())
              .authenticationManager(authenticationManager);
  }
  ```
- Buat methode (auth-server/src/main/java/domain/config/KonfigurasiAuthorizationServer.java/AuthorizationServerConfiguration). Methode ini berfungsi untuk menentukan role apa saja yang dapat mendapat mengakses check token
  ```
  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
      oauthServer.checkTokenAccess("hasRole('CLIENT')");
  }
  ```
- Buat methode (auth-server/src/main/java/domain/config/KonfigurasiAuthorizationServer.java/AuthorizationServerConfiguration). Methode ini berisi list role client berasarkan grant tye
  ```
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
      .inMemory()
      .withClient("clientauthcode")
        .secret("123456")
        .authorizedGrantTypes("authorization_code","refresh_token")
        .authorities("CLIENT")
        .scopes("read","write")
        .resourceIds(RESOURCE_ID)
      .and()
      .withClient("clientcred")
        .secret("123456")
        .authorizedGrantTypes("client_credentials")
        .scopes("trust")
        .resourceIds(RESOURCE_ID)
      .and()
      .withClient("clientapp")
        .secret("123456")
        .authorizedGrantTypes("password")
        .scopes("read","write")
        .resourceIds(RESOURCE_ID)  
      .and()
      .withClient("jsclient")
        .secret("123456")
        .authorizedGrantTypes("implicit")
        .authorities("CLIENT")
        .scopes("read","write")
        .resourceIds(RESOURCE_ID)
        .redirectUris("http://localhost:20000/implicit-client")
        .accessTokenValiditySeconds(60* 60 *24)
        .autoApprove(true);
  }
  ```
- Konfigurasi Security (auth-server/src/main/java/domain/config/KonfigurasiSecurity)
  ```
  @Override
      @Bean
      public AuthenticationManager authenticationManagerBean() throws Exception {
          return super.authenticationManagerBean();
      }
  ```
  

# Flow untuk masing masing role clint #
Perlu diketahui authorization server akan jalan di port 10000 dan resource server akan jalan di port 8080. OAuth 2 sendiri menyediakan 4 role yaitu :
- Authorization Code (Akses yang dalam mendapatkan resource memerlukan kode)
- Username Password
- Client Credential (Akses yang memungkinkan client tidak perlu login untuk mendapatkan akses)
- Client Impicit (Akses yang biasanya digunakan untuk mendapatkan resource berupa js)

## Authorization Code ##
Karena kita belum memiliki aplikasi client maka kita akan langsung redirect ke authorization server dengan memasukkan url
  ```
    http://localhost:10000/oauth/authorize?client_id=clientauthcode&response_type=code&redirect_uri=http://localhost:8080/api/halo
  ```
  Note : url tersebut akan diproses oleh auth server (http://localhost:10000/oauth/authorize) dengan parameter client_id yaitu `clientauthcode`, response_type berupa `code` dan ingin request ke `http://localhost:8080/api/halo`
- Kita akan langsung di redirect ke `http://localhost:10000/login`. Kita diminta untuk memasukkan otorisasi login
- Lalu akan muncul form approval dengan scope `read` dan `write`, untuk approval ini dapat dikonfigurasi agar tidak tampil atau otomatis authorize.
- Lalu akan muncul url sebagai berikut
  ```
    http://localhost:8080/api/halo?code=Ixd8e
  ```
  Kemudian kita ambil code tersebut
- Lalu ditukarkan kode tersebut dengan access token dengan cara mengakses kembali ke auth server dengan mencantumkan
  - Methode HTTP berupa `POST`
  - client_id berupa `clientauthcode` dan secret `123456`
  - Url berupa `http://localhost:10000/oauth/token`
  - Header berupa `application/json`
  - data yaitu:
  	- grant_type berupa `authorization_code`
  	- code berupa `Ixd8e`
  	- redirect_uri berupa `http://localhost:8080/api/halo`
- Setelah itu kita akan mendapatkan data sebagai berikut :
  - access_token : `blablabla`
  - token_type : `bearer`
  - refresh_token : `baba`
  - expires_in : `43199`
  - scope : `read`,`write`
- Lalu akses ke resource server dengan menambahkan access token yang telah kita dapatkan tadi dengan url `http://localhost:8080/api/halo?access_token=blablabla`
- Selamat anda sudah dapat mengakses resource.
	Sebenarnya sebelum token mendapatkan resource tersebut resource server melakukan pengecekan ke authorization server terhadap acces token tadi apakah valid atau tidak.Namun perlu diketahui bahwa saat ini kedua aplikasi berjalan di port yang berbeda (auth server port 10000, dan resource server di port 8080) sehingga tidak dapat sharing token via memory.
Solusinya adalah :
- Token tadi akan disimpan ke database menggunakan jdbc token store oleh auth server. Lalu resource server akan diarahkan ke database tersebut. Tetapi ada kalanya resource server tidak dapat mendapatkan akses ke database.
- Menggunakan RemoteTokenServices yang ada pada setting KonfigurasiResourceServer
  ```
  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
      RemoteTokenServices tokenService = new RemoteTokenServices();
      tokenService.setClientId("clientauthcode");
      tokenService.setClientSecret("123456");
      tokenService.setCheckTokenEndpointUrl("http://localhost:10000/oauth/check_token");
      resources
              .resourceId(RESOURCE_ID)
              .tokenServices(tokenService);
  }
  ```
  resource server akan mengarahkan ke url `http://localhost:10000/oauth/check_token` dengan menambahkan client id berupa `clientauthcode`, secret berupa `123456` dan access_token menggunakn methode GET. sehingga resource server mendapat data sebagai berikut :
  - rid :  `belajar`
  - exp : `14441`
  - username : `endy`
  - authorities : `[ROLE_OPERATOR]`,`[ROLE_SUPERVISOR]`
  - client_id : `clientauthcode`
  - scope : `read`,`write`
  dari sini resource server tahu role dari user tersebut dan dapat memutuskan user tersebut dapat mengakses resource tersebut atau tidak

## Username Password ##
- Kita akses auth server dengan mencantumkan
  - Methode HTTP berupa `POST`
  - client_id berupa `clientapp` dan secret `123456`
  - Url berupa `http://localhost:10000/oauth/token`
  - Header berupa `application/json`
  - data yaitu:
  	- client_id berupa `clientapp`
  	- grant_type berupa `password`
  	- username berupa `endy`
    - password berupa `123`
- Setelah itu kita akan mendapatkan data sebagai berikut :
  - access_token : `blablabla`
  - token_type : `bearer`
  - expires_in : `43199`
  - scope : `read`,`write`
- Kemudian Kita akses auth server dengan mencantumkan
  - Methode HTTP berupa `GET`
  - client_id berupa `clientapp` dan secret `123456` dan authorization header `Bearer blablabla`
  - Url berupa `http://localhost:8080/api/halo`
  - Header berupa `application/json`
- Selamat anda sudah dapat mengakses resource.

## Client Credential ##
- Kita akses auth server dengan mencantumkan
  - Methode HTTP berupa `POST`
  - client_id berupa `clientcred` dan secret `123456`
  - Url berupa `http://localhost:10000/oauth/token`
  - Header berupa `application/json`
  - data yaitu:
  	- client_id berupa `clientcred`
  	- grant_type berupa `client_credentials`
- Setelah itu kita akan mendapatkan data sebagai berikut :
  - access_token : `blablabla`
  - token_type : `bearer`
  - expires_in : `43199`
  - scope : `read`,`write`
- Kemudian Kita akses auth server dengan mencantumkan
  - Methode HTTP berupa `GET`
  - client_id berupa `clientcred` dan secret `123456` dan authorization header `Bearer blablabla`
  - Url berupa `http://localhost:8080/api/halo`
  - Header berupa `application/json`
  - data yaitu:
  	- client_id berupa `clientcred`
- Perlu diketahui karena client cred ini tidak menggunakan username dan password maka dia tidak dapat digunakan dalam aplikasi ini karena resource server telah kita setting hanya untuk menerima authentifikasi dari user dengan role operator.

## Implicit Client ##
- Karena kita belum memiliki aplikasi client maka kita akan langsung redirect ke authorization server dengan memasukkan url
  ```
    http://localhost:10000/oauth/authorize?client_id=jsclient&response_type=token&redirect_uri=http://localhost:2000/implicit-client
  ```
  Note : url tersebut akan diproses oleh auth server (http://localhost:10000/oauth/authorize) dengan parameter client_id yaitu `jsclient`, response_type berupa `token` dan ingin request ke `http://localhost:2000/implicit-client`
- Kita akan langsung di redirect ke `http://localhost:10000/login`. Kita diminta untuk memasukkan otorisasi login
- Selanjutnya kita akan mendapatkan url `http://localhost:2000/implicit-client/#access_token=blabla&token_type=bearer`
- Kemudian Kita akses auth server dengan mencantumkan
  - Methode HTTP berupa `GET`
  authorization header `Bearer blablabla`
  - Url berupa `http://localhost:8080/api/halo`
  - Header berupa `application/json`

# Mendapatkan Informasi User #
- Pertama class buat controller(resource-server/src/main/java/domain/controller/InfoController)
  ```
    @RestController
    @EnableResourceServer
    public class InfoController{

    }
  ```
- Buat methode untuk mengakses info user
  ```
    @RequestMapping("/userinfo")
    public Principal user info (Principal principal){
      return principal;
    }
  ```
- Untuk mengaksesnya kita login dulu dengan menggunakan grant type clientauthcode sampai kita mendapatkan access tokenya
- Setelah mendapatkanya masukkan url `http://localhost:8080/userinfo?access_token=blabla`

# Membuat Aplikasi Client #
Pertama kita akan membuat aplikasi client authcode sedehana menggunakan Spring Framework.
Step by Step nya adalah sebagai berikut :
- Download Setup projek di `http://start.spring.io`
- Isi projek metadata berupa :
  * artifact id :
  * groupId :
- Isi dependency yang dubutuhkan berupa :
  * web
  * thymeleaf
- Download lalu pindahkan ke text editor
- Kita coba bikin Controller sebagi resources (client-authcode/src/main/java/domain/controller/HaloController)
  ```
  @RestContoller
  public class HaloController {

  }
  ```
- Kita bikin methode untuk di proses ke user. Methode ini akan mengembalikan waktu sekarang menggunakan variabel `waktu`. methode ini dapat diakses di url /halo.(client-authcode/src/main/java/domain/controller/HaloController)
  ```
    @RequestMapping("/halo")
    public void halo(Model m, @RequestParam(required = false) String nama){
        m.addAttribute("waktu", new Date());
        if(nama != null && !nama.isEmpty()){
            m.addAttribute("pesan", "Halo "+nama);
        }
    }
  ```
- Kita bikin UI untuk mengeluarkan informasi dari methode halo yang telah buat tadi.(client-authcode/src/main/resources/templates/halo.html)
  ```
    <html>
        <head>
            <title>Halo Spring Boot</title>
        </head>
        <body>
            <h1>Halo Spring Boot</h1>
            <h2>Waktu saat ini : <span th:text="${waktu}"></span></h2>
            <h3>Pesan : <span th:text="*{pesan}?:''">Halo</span></h3>
        </body>
    </html>
  ```
# Proteksi Resource Server Dengan Spring Security
- Lalu coba kita proteksi Resource Server kita menggunakan Spring Security dengan menambahkan dependency di pom.xml. (client-authcode/pom.xml)
  ```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.security.oauth</groupId>
        <artifactId>spring-security-oauth2</artifactId>
        <version>2.0.7.RELEASE</version>
    </dependency>
  ```
  Pada keadaan ini kita akan diminta otorisasi jika kita mengakses di browser dengan username : `user` dan password yang ada pada CLI. karena tidak efisien mengabaikan otorisasi.
- Buat class untuk konfigurasi otorisasi (/client-authcode/src/main/java/domain/config/KonfigurasiSecurity)
  ```
  @Configuration
  public class KonfigurasiSecurity  extends WebSecurityConfigurerAdapter {

  }
  ```
- Override methode configure (/client-authcode/src/main/java/domain/config/KonfigurasiSecurity). Methode untuk mengabaikan otorisasi
  ```
    @Override
    public void configure (WebSecurity web) throws Exception{
      web.ignoring().anyRequest();
    }
  ```
- Buat class untuk konfigurasi OAuth(/client-authcode/src/main/java/domain/config/KonfigurasiOauth2Client).
  ```
    @Configuration
    @EnableOAuth2Client
    public class KonfigurasiOauth2Client{

    }
  ```
-
  ```
  private String urlAuthorization = "http://localhost:10000/oauth/authorize";
  private String urlToken = "http://localhost:10000/oauth/token";

  @Bean
  public OAuth2RestOperations oauthClient(OAuth2ClientContext context){
      OAuth2RestTemplate restClient = new OAuth2RestTemplate(resource(), context);
      return restClient;
  }

  @Bean
  public OAuth2ProtectedResourceDetails resource(){
      AuthorizationCodeResourceDetails authCodeClient
              = new AuthorizationCodeResourceDetails();
      authCodeClient.setClientId("clientauthcode");
      authCodeClient.setClientSecret("123456");
      authCodeClient.setUserAuthorizationUri(urlAuthorization);
      authCodeClient.setAccessTokenUri(urlToken);
      return authCodeClient;
  }
  ```
-  a
  ```
  Map<String, Object> hasil = oauthClient.getForObject(apiHalo, HashMap.class);
  System.out.println("Waktu dari server : "+hasil.get("waktu"));
  m.addAttribute("waktu", new Date().toString());
  ```
