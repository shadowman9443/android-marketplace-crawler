# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 20110209224341) do

  create_table "app_permissions", :force => true do |t|
    t.integer  "app_id"
    t.integer  "permission_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "app_permissions", ["app_id"], :name => "index_app_permissions_on_app_id"
  add_index "app_permissions", ["permission_id"], :name => "index_app_permissions_on_permission_id"

  create_table "app_targets", :force => true do |t|
    t.integer  "app_id"
    t.integer  "target_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "app_targets", ["app_id"], :name => "index_app_targets_on_app_id"
  add_index "app_targets", ["target_id"], :name => "index_app_targets_on_target_id"

  create_table "apps", :force => true do |t|
    t.string   "creator"
    t.string   "packageName"
    t.string   "title"
    t.text     "description"
    t.string   "appId"
    t.string   "category"
    t.text     "recentChanges"
    t.string   "email"
    t.string   "phone"
    t.string   "website"
    t.string   "version"
    t.integer  "versionCode"
    t.string   "appType"
    t.text     "promoText"
    t.string   "promoVideo"
    t.integer  "screenshotCount"
    t.string   "price"
    t.string   "priceCurrency"
    t.string   "installSize"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "apps", ["packageName"], :name => "index_apps_on_packagename", :unique => true

  create_table "comments", :force => true do |t|
    t.integer  "rating"
    t.string   "creationTime"
    t.string   "authorName"
    t.string   "text"
    t.string   "authorId"
    t.integer  "app_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "comments", ["app_id"], :name => "index_comments_on_app_id"
  add_index "comments", ["authorId"], :name => "index_comments_on_authorId"

  create_table "permissions", :force => true do |t|
    t.string   "name"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "permissions", ["name"], :name => "index_permissions_on_name", :unique => true

  create_table "ratings", :force => true do |t|
    t.string   "rating"
    t.integer  "ratingCount"
    t.integer  "downloadCount"
    t.string   "downloadCountText"
    t.integer  "app_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "ratings", ["app_id"], :name => "index_ratings_on_app_id", :unique => true

  create_table "targets", :force => true do |t|
    t.string   "name"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "targets", ["name"], :name => "index_targets_on_name", :unique => true

  create_table "versions", :force => true do |t|
    t.integer  "versioned_id"
    t.string   "versioned_type"
    t.integer  "user_id"
    t.string   "user_type"
    t.string   "user_name"
    t.text     "modifications"
    t.integer  "number"
    t.integer  "reverted_from"
    t.string   "tag"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "versions", ["created_at"], :name => "index_versions_on_created_at"
  add_index "versions", ["number"], :name => "index_versions_on_number"
  add_index "versions", ["tag"], :name => "index_versions_on_tag"
  add_index "versions", ["user_id", "user_type"], :name => "index_versions_on_user_id_and_user_type"
  add_index "versions", ["user_name"], :name => "index_versions_on_user_name"
  add_index "versions", ["versioned_id", "versioned_type"], :name => "index_versions_on_versioned_id_and_versioned_type"

  create_table "visuals", :force => true do |t|
    t.integer  "app_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "image_file_name"
    t.string   "image_content_type"
    t.integer  "image_file_size"
    t.datetime "image_updated_at"
  end

  add_index "visuals", ["app_id"], :name => "index_visuals_on_app_id"

end
