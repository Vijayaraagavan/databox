// showSnack({
//     message: "Logged in successfully",
//     color: "bg-green-500",
//   });

const showSide = () => {
    const el = document.getElementById("sidebar");
    const icon = document.getElementById("sideicon");
    el.classList.toggle("sidebar");
    // icon.classList.toggle("pl-80");
  };
  const logout = () => {
    fetch("http://localhost:8081/api/logout", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("token"),
      },
    }).then((resp) => {
      if (resp.status == 200) {
        window.location.href = "/login";
        return;
      }
      console.log(resp);
    })
  }