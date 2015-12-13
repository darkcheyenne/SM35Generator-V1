/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function setHeight(){
    if (document.getElementById('j_idt7:input').value == ""){
        document.getElementById('j_idt7:input').style.height = '150px';
    }
    
    document.getElementById('j_idt7:input').style.height = document.getElementById('j_idt7:input').scrollHeight+'px';
}