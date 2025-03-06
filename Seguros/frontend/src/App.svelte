<script>
  import { Router, Route } from "svelte-routing";
  import Home from "./routes/Home.svelte";
  import Login from "./routes/Login.svelte";
  import Clientes from "./routes/Clientes.svelte";
</script>

<Router>
  <Route path="/" component={Home} />
  <Route path="/login" component={Login} />
  <Route path="/clientes" component={Clientes} />
</Router>
