class CreateApps < ActiveRecord::Migration
  def self.up
    create_table :apps do |t|
      t.string :creator
      t.string :packageName
      t.string :title
      t.text :description
      t.string :appId
      t.string :category
      t.text :recentChanges
      t.string :email
      t.string :phone
      t.string :website
      t.string :version
      t.integer :versionCode
      t.string :appType
      t.text :promoText
      t.string :promoVideo
      t.integer :screenshotCount
      t.string :price
      t.string :priceCurrency
      t.string :installSize

      t.timestamps
    end
    add_index :apps, :packagename, :unique => true
  end

  def self.down
    drop_table :apps
  end
end
