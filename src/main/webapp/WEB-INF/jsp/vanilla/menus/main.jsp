<h1>main menu</h1>
<ul>
  
  <%-- Home --%>
  <li><@menulinks "menu-main" "1" "0" "${menu1_active}menu-tab"/></li>
  
  
  <%-- Explore --%>
  <li>
    
    <@menulinks "menu-main" "2" "0" "${menu2_active}dropdown menu-tab"/>
      
      <ul>
        
        <li><@menulinks "menu-main" "2" "1" "menu-sub-tab"/></li>
        <li><@menulinks "menu-main" "2" "2" "menu-sub-tab"/></li>
        <li><@menulinks "menu-main" "2" "3" "menu-sub-tab"/></li>
        <li><@menulinks "menu-main" "2" "4" "menu-sub-tab"/></li>
        <li><@menulinks "menu-main" "2" "5" "menu-sub-tab"/></li>
        <li><@menulinks "menu-main" "2" "6" "menu-sub-tab"/></li>
        
      </ul>
      
  </li>
  
  
  <%-- Help --%>
  <li>
    
    <@menulinks "menu-main" "3" "0" "${menu3_active}dropdown menu-tab"/>
    
    <ul>
      
      <li><@menulinks "menu-main" "3" "1" "menu-sub-tab"/></li>
      <li><@menulinks "menu-main" "3" "2" "menu-sub-tab"/></li>
      <li><@menulinks "menu-main" "3" "3" "menu-sub-tab"/></li>
      <li><@menulinks "menu-main" "3" "4" "menu-sub-tab"/></li>
      <li><@menulinks "menu-main" "3" "5" "menu-sub-tab"/></li>
      
    </ul>
    
  </li>
  
  
  <%-- About Us --%>
  <li>
    
    <@menulinks "menu-main" "4" "0" "${menu4_active}dropdown menu-tab"/>
    
    <ul>
      
      <li><@menulinks "menu-main" "4" "1" "menu-sub-tab"/></li>
      <li><@menulinks "menu-main" "4" "2" "menu-sub-tab"/></li>
      <li><@menulinks "menu-main" "4" "3" "menu-sub-tab"/></li>
      <li><@menulinks "menu-main" "4" "4" "menu-sub-tab"/></li>
      
    </ul>
    
  </li>
  
  
  <%-- Follow Us --%>
  <li>
    
    <@menulinks "menu-main" "5" "0" "${menu5_active}dropdown menu-tab"/>
    
    <ul>
      
      <li><@menulinks "menu-main" "5" "1" "menu-sub-tab"/></li>
      <li><@menulinks "menu-main" "5" "2" "menu-sub-tab"/></li>
      <li><@menulinks "menu-main" "5" "3" "menu-sub-tab external"/></li>
      <li><@menulinks "menu-main" "5" "4" "menu-sub-tab external"/></li>
      
    </ul>
    
  </li>


  <%-- My Europeana --%>
  <li><@menulinks "menu-main" "6" "0" "${menu6_active}menu-tab"/></li>
  
  
  <%-- Choose a language --%>
  <li>
    
    <#include '/_common/html/menus/language.ftl'/>
    
  </li>
  
</ul>