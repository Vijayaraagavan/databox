console.log("snacked");
let snackData;
const showSnack = (payload) => {
  clearSnack();
  Object.assign(snackData, payload);
  const txt = document.getElementById("snack-text");
  txt.innerHTML = snackData.message;
  const el = document.getElementById("snack");
  el.classList.remove("animate-y");
  el.classList.add(snackData.color);
  setTimeout(() => {
    el.classList.add("animate-y");
  }, snackData.timeout || 2000);
};

const hideSnack = () => {
  const el = document.getElementById("snack");
  el.classList.add("animate-y");
};
const clearSnack = () => {
  snackData = {
    message: "Error",
    color: "bg-green-500",
    timeout: 2000,
  };
};
