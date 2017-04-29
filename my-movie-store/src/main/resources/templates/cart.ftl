<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorator="layout/default">
<body>
<div class="row" layout:fragment="content">
  <ul class="collection" th:each="cart : ${cartItems}">
    <li class="collection-item" th:text="${session}">Session</li>
  </ul>
</div>
</body>
</html>
