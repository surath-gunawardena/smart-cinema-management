const messages = [
  { type: "success", text: "[[${success}]]" },
  { type: "info", text: "[[${info}]]" },
  { type: "warn", text: "[[${warn}]]" },
  { type: "error", text: "[[${error}]]" },
  {
    type: "logout",
    text: "[[${param.logout} ? 'You have been logged out.' : '']]",
  },
];

const container = document.getElementById("toast-container");

messages.forEach((msg) => {
  if (msg.text && msg.text.trim() !== "" && msg.text !== "null") {
    const div = document.createElement("div");
    div.className =
      "toast relative flex items-start gap-2 px-4 py-3 rounded-lg shadow-lg border text-sm w-72 backdrop-blur-md " +
      (msg.type === "success"
        ? "bg-emerald-900/70 border-emerald-700 text-emerald-200"
        : msg.type === "info"
        ? "bg-blue-900/70 border-blue-700 text-blue-200"
        : msg.type === "warn"
        ? "bg-amber-900/70 border-amber-700 text-amber-200"
        : msg.type === "error"
        ? "bg-rose-900/70 border-rose-700 text-rose-200"
        : "bg-blue-900/70 border-blue-700 text-blue-200");

    div.innerHTML = `
              <div class="flex-1">${msg.text}</div>
              <button class="text-slate-400 hover:text-white ml-2 text-lg leading-none">&times;</button>
            `;

    // Close button
    div.querySelector("button").addEventListener("click", () => {
      div.classList.add("hide");
      setTimeout(() => div.remove(), 400);
    });

    container.appendChild(div);

    // Auto-dismiss after 4 seconds
    setTimeout(() => {
      div.classList.add("hide");
      setTimeout(() => div.remove(), 400);
    }, 4000);
  }
});
