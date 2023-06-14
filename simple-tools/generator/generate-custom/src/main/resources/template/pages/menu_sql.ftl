

INSERT INTO sys_menu
(id, pid, name, url, perm, icon, seq, remark, `type`, create_time, create_by, update_time, update_by)
VALUES(001, 上级ID, '${tableClass.remarks}', '${tableClass.pagesPath}/${tableClass.simpleTableName}', '', '', 1, NULL, 0, '${generateDate}', 'admin', '${generateDate}', 'admin');

INSERT INTO sys_menu
(id, pid, name, url, perm, icon, seq, remark, `type`, create_time, create_by, update_time, update_by)
VALUES(001, 权限归属ID, '添加', '', '${tableClass.pagesPath?replace("/", ":")}:add', '', 1, NULL, 1, '${generateDate}', 'admin', '${generateDate}', 'admin');

INSERT INTO sys_menu
(id, pid, name, url, perm, icon, seq, remark, `type`, create_time, create_by, update_time, update_by)
VALUES(001, 权限归属ID, '修改', '', '${tableClass.pagesPath?replace("/", ":")}:upd', '', 1, NULL, 1, '${generateDate}', 'admin', '${generateDate}', 'admin');

INSERT INTO sys_menu
(id, pid, name, url, perm, icon, seq, remark, `type`, create_time, create_by, update_time, update_by)
VALUES(001, 权限归属ID, '查看', '', '${tableClass.pagesPath?replace("/", ":")}:info', '', 1, NULL, 1, '${generateDate}', 'admin', '${generateDate}', 'admin');

INSERT INTO sys_menu
(id, pid, name, url, perm, icon, seq, remark, `type`, create_time, create_by, update_time, update_by)
VALUES(001, 权限归属ID, '删除', '', '${tableClass.pagesPath?replace("/", ":")}:del', '', 1, NULL, 1, '${generateDate}', 'admin', '${generateDate}', 'admin');
