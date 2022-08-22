db = db.getSiblingDB('ticket_db')
db.tickets.find({"orderId": "1"}, {"orderId": 1, "_id": 1})