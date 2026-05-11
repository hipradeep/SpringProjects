## Early Hibernate — XML-Based Mapping

Hibernate was the first dominant ORM framework for Java. It used `.hbm.xml` files for object-to-table mapping, keeping mapping configuration separate from Java classes.

```xml
<class name="Product" table="products">
    <id name="id"/>
    <property name="name"/>
</class>
```

**Core Hibernate APIs:**

| Component | Purpose | Lifecycle |
| --- | --- | --- |
| `SessionFactory` | Thread-safe, heavyweight factory; manages DB connection pool | Application startup to shutdown |
| `Session` | Lightweight; represents a single request/transaction context | One per request/transaction |
| `Transaction` | Wraps DB operations; ensures ACID compliance | Explicit begin/commit/rollback |
| `Query` | Executes HQL or native SQL queries | Per query execution |

---

**Problems with XML-Based Hibernate:**

- Too much XML boilerplate
- Hard to maintain — mapping and code in separate files
- Vendor-specific configuration
- Tight coupling to Hibernate

### Example Usage

The example implemented in this branch uses the native Hibernate APIs strictly (no JPA), showing how things were done before JPA standardization.

1. **Entity (`Product.java`)**: A simple POJO with no annotations.
2. **Mapping (`Product.hbm.xml`)**: Maps the `Product` class to the `products` table.
3. **Configuration (`hibernate.cfg.xml`)**: Defines database connections and includes the mapping files.
4. **Runner (`HibernateXmlExample.java`)**: 
   - Uses `new Configuration().configure("hibernate.cfg.xml").buildSessionFactory()` to bootstrap Hibernate.
   - Uses `sessionFactory.openSession()` and `session.beginTransaction()` to start transactions.
   - Uses `session.persist()` (or `save()`) and `session.createQuery()` to manipulate data.
